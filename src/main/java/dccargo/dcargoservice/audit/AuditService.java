package dccargo.dcargoservice.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dccargo.dcargoservice.model.dcargo.AuditLog;
import dccargo.dcargoservice.repository.dcargo.AuditLogRepository;
import jakarta.persistence.Id;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Накопитель аудит-записей: получает промежуточные записи от перехватчика Hibernate
 * через {@link AuditContext} и сохраняет их при закрытии операции. Здесь
 * финализируются снапшоты (сериализация в JSON) и id записи (для INSERT, где
 * id приходит только после самого вставки в БД).
 */
@Service
public class AuditService {

    private final AuditLogRepository repository;
    private final ObjectMapper objectMapper;
    private final EntityParentResolver parentResolver;

    public AuditService(AuditLogRepository repository, ObjectMapper objectMapper, EntityParentResolver parentResolver) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.parentResolver = parentResolver;
    }

    /**
     * Сохраняет все записи закрытой операции.
     * Вызывается аспектом при выходе из @Audited-метода.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void flush(AuditContext.Tx tx) {
        List<AuditContext.PendingEntry> pending = tx.getPending();
        if (pending == null || pending.isEmpty()) {
            return;
        }
        for (AuditContext.PendingEntry pe : pending) {
            AuditLog entry = pe.getEntry();

            Long entityId = toLong(pe.getId());
            String idProperty = idPropertyName(pe.getEntity());
            if (entityId == null) {
                entityId = readIdValue(pe.getEntity(), idProperty);
            }
            if (entry.getEntityId() == null && entityId != null) {
                entry.setEntityId(entityId);
            }
            patchId(pe.getBefore(), idProperty, entityId);
            patchId(pe.getAfter(), idProperty, entityId);

            entry.setTransactionId(tx.getTransactionId());
            entry.setSeq(tx.nextSeq());
            entry.setOperation(tx.getOperation());
            entry.setActorId(tx.getActorId());
            entry.setActorName(tx.getActorName());
            entry.setEndpoint(tx.getEndpoint());
            entry.setSnapshotBefore(toJson(pe.getBefore()));
            entry.setSnapshotAfter(toJson(pe.getAfter()));
            EntityParentResolver.ParentInfo parent = parentResolver.resolve(pe.getEntity());
            if (parent != null) {
                entry.setParentEntityType(parent.parentType());
                entry.setParentEntityId(parent.parentId());
            }
            repository.save(entry);
        }
    }

    private void patchId(Map<String, Object> snapshot, String idProperty, Long entityId) {
        if (snapshot != null && entityId != null && idProperty != null) {
            snapshot.put(idProperty, entityId);
        }
    }

    private String toJson(Map<String, Object> snapshot) {
        if (snapshot == null || snapshot.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(snapshot);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private Long toLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

    /** Имя поля сущности с аннотацией @Id (id, idUser, idOrder...). */
    private String idPropertyName(Object entity) {
        if (entity == null) {
            return null;
        }
        Class<?> type = entityClass(entity);
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        for (Method method : type.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Id.class) && method.getName().startsWith("get")) {
                return decapitalize(method.getName().substring(3));
            }
        }
        return null;
    }

    /** Читает значение id-поля с сущности (работает и для Hibernate-прокси). */
    private Long readIdValue(Object entity, String idProperty) {
        if (entity == null || idProperty == null) {
            return null;
        }
        Class<?> type = entityClass(entity);
        Object value = null;
        try {
            Field field = type.getDeclaredField(idProperty);
            field.setAccessible(true);
            value = field.get(entity);
        } catch (Exception e) {
            try {
                Method getter = type.getMethod("get" + capitalize(idProperty));
                value = getter.invoke(entity);
            } catch (Exception ex) {
                return null;
            }
        }
        return toLong(value);
    }

    private Class<?> entityClass(Object entity) {
        if (entity instanceof HibernateProxy) {
            return ((HibernateProxy) entity).getHibernateLazyInitializer().getPersistentClass();
        }
        return entity.getClass();
    }

    private String capitalize(String name) {
        return name.isEmpty() ? name : Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private String decapitalize(String name) {
        return name.isEmpty() ? name : Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
}