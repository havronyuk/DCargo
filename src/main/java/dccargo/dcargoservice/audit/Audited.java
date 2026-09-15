package dccargo.dcargoservice.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает метод сервиса, вокруг которого открывается аудит-операция.
 * Все изменения сущностей в рамках вызова (включая каскадные save) будут
 * сгруппированы одной transaction_id.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audited {

    /** Бизнес-операция: UPDATE_TRUCK, ASSIGN_TRUCK_TO_ORDER, CANCEL... */
    String operation() default "";
}