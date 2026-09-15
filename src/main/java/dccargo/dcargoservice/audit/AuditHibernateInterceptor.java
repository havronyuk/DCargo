package dccargo.dcargoservice.audit;

import dccargo.dcargoservice.model.dcargo.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Interceptor;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.type.AssociationType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.time.temporal.TemporalAccessor;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Побочный перехватчик всех операций записи/удаления сущностей.
 * Работает сам по себе, без аннотаций над методами: Hibernate вызывает его
 * при каждом сохранении/обновлении/удалении внутри JPA-транзакции.
 * Собирает «было/стало» только если в потоке открыт аудит-контекст {@link AuditContext}
 * (то есть вызов идёт из метода, помеченного {@link Audited}).
 */
@Slf4j
@Component
public class AuditHibernateInterceptor implements Interceptor {

    /**
     * Перехват работает внутри flush бизнес-транзакции, поэтому любую его
     * ошибку глотаем в консоль: она не должна ронять сам бизнес-запрос.
     */
    @Override
    public boolean onSave(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) {
        try {
            record("INSERT", entity, id, null, stateMap(propertyNames, state, types));
        } catch (Exception e) {
            log.error("Аудит: ошибка в onSave для {}", entity, e);
        }
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        try {
            record("UPDATE", entity, id,
                    stateMap(propertyNames, previousState, types),
                    stateMap(propertyNames, currentState, types));
        } catch (Exception e) {
            log.error("Аудит: ошибка в onFlushDirty для {}", entity, e);
        }
        return false;
    }

    @Override
    public void onDelete(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) {
        try {
            record("DELETE", entity, id, stateMap(propertyNames, state, types), null);
        } catch (Exception e) {
            log.error("Аудит: ошибка в onDelete для {}", entity, e);
        }
    }

    private void record(String changeType, Object entity, Object id, Map<String, Object> before, Map<String, Object> after) {
        if (entity instanceof AuditLog) {
            return;
        }
        AuditContext.Tx tx = AuditContext.current();
        if (tx == null) {
            return;
        }
        AuditLog entry = new AuditLog();
        entry.setChangeType(changeType);
        entry.setEntityType(entity.getClass().getSimpleName());
        tx.addPending(new AuditContext.PendingEntry(entry, entity, id, before, after));
    }

    /**
     * Снимок скалярных полей записи: связи (коллекции, @ManyToOne/@OneToMany)
     * пропускаются — каждая связанная сущность пишется отдельной аудит-строкой.
     */
    private Map<String, Object> stateMap(String[] propertyNames, Object[] state, Type[] types) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (state == null) {
            return map;
        }
        for (int i = 0; i < propertyNames.length; i++) {
            if (types != null && types[i] instanceof AssociationType) {
                continue;
            }
            map.put(propertyNames[i], toScalar(state[i]));
        }
        return map;
    }

    private Object toScalar(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof PersistentCollection) {
            return null;
        }
        if (value instanceof TemporalAccessor) {
            return value.toString();
        }
        if (value instanceof Enum) {
            return ((Enum<?>) value).name();
        }
        if (value instanceof byte[]) {
            return ((byte[]) value).length;
        }
        return value;
    }
}