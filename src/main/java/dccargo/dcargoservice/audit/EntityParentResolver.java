package dccargo.dcargoservice.audit;

import dccargo.dcargoservice.model.dcargo.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Определяет «материнскую» сущность для зависимой записи.
 * Логика простая: у заведомо зависимых сущностей есть FK-поле
 * (например TruckDocument.truckId, UserDocument.userId, Refueling.idRouteSheet),
 * которое и даёт parent_entity_type / parent_entity_id.
 * <p>
 * Незарегистрированные сущности считаются корневыми — родитель пуст.
 */
@Component
public class EntityParentResolver {

    /** Информация о родителе. */
    public static class ParentInfo {
        private final String parentType;
        private final Long parentId;

        private ParentInfo(String parentType, Long parentId) {
            this.parentType = parentType;
            this.parentId = parentId;
        }

        public String parentType() {
            return parentType;
        }

        public Long parentId() {
            return parentId;
        }
    }

    /** Описание родителя для типа записи: тип родителя + имя FK-поля. */
    private static class ParentDef {
        private final Class<?> parentType;
        private final String idField;

        ParentDef(Class<?> parentType, String idField) {
            this.parentType = parentType;
            this.idField = idField;
        }
    }

    /** Тип записи → (тип родителя + имя FK-поля). */
    private static final Map<Class<?>, ParentDef> PARENTS = buildParentMap();

    private static Map<Class<?>, ParentDef> buildParentMap() {
        Map<Class<?>, ParentDef> map = new HashMap<>();
        map.put(TruckDocument.class, new ParentDef(Truck.class, "truckId"));
        map.put(TruckEquipment.class, new ParentDef(Truck.class, "truckId"));
        map.put(TruckTire.class, new ParentDef(Truck.class, "truckId"));
        map.put(Refueling.class, new ParentDef(RouteSheet.class, "idRouteSheet"));
        map.put(RouteSheet.class, new ParentDef(TruckUserAssignment.class, "idTruckUserAssignment"));
        map.put(OrderTruck.class, new ParentDef(Order.class, "idOrder"));
        map.put(TruckUserAssignment.class, new ParentDef(Truck.class, "truckId"));
        map.put(OrderPoint.class, new ParentDef(Order.class, "idOrder"));
        map.put(UserDocument.class, new ParentDef(User.class, "userId"));
        map.put(DriverCard.class, new ParentDef(User.class, "idUser"));
        map.put(Passport.class, new ParentDef(User.class, "idUser"));
        return map;
    }

    public ParentInfo resolve(Object entity) {
        if (entity == null) {
            return null;
        }
        ParentDef def = PARENTS.get(entityClass(entity));
        if (def == null) {
            return null;
        }
        Long parentId = readIdValue(entity, def.idField);
        if (parentId == null) {
            return null;
        }
        return new ParentInfo(def.parentType.getSimpleName(), parentId);
    }

    private Long readIdValue(Object entity, String fieldName) {
        Class<?> type = entityClass(entity);
        try {
            Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
            return toLong(field.get(entity));
        } catch (Exception e) {
            try {
                Method getter = type.getMethod("get" + capitalize(fieldName));
                return toLong(getter.invoke(entity));
            } catch (Exception ex) {
                return null;
            }
        }
    }

    private Class<?> entityClass(Object entity) {
        if (entity instanceof HibernateProxy) {
            return ((HibernateProxy) entity).getHibernateLazyInitializer().getPersistentClass();
        }
        return entity.getClass();
    }

    private Long toLong(Object value) {
        return value instanceof Number ? ((Number) value).longValue() : null;
    }

    private String capitalize(String name) {
        return name.isEmpty() ? name : Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}