package dccargo.dcargoservice.model.dcargo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Ключ группы: все строки одной пользовательской операции (один клик). */
    @Column(name = "transaction_id", nullable = false, length = 36)
    private String transactionId;

    /** Порядок строк в рамках операции (1, 2, 3...). */
    @Column(name = "seq", nullable = false)
    private Integer seq;

    /** Бизнес-операция: UPDATE_TRUCK, ASSIGN_TRUCK_TO_ORDER, CANCEL... */
    @Column(name = "operation", nullable = false, length = 100)
    private String operation;

    /** Тип изменения записи: INSERT / UPDATE / DELETE. */
    @Column(name = "change_type", nullable = false, length = 20)
    private String changeType;

    /** Имя Java-модели изменения: Truck, Order, RouteSheet... */
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;

    /** ID изменённой записи. */
    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    /** Человекочитаемое имя записи для отчётов: МАЗ / 1070AB. */
    @Column(name = "entity_label", length = 200)
    private String entityLabel;

    /** Материнская Java-модель, если запись зависимая (документ у трака). */
    @Column(name = "parent_entity_type", length = 50)
    private String parentEntityType;

    /** ID материнской записи. */
    @Column(name = "parent_entity_id")
    private Long parentEntityId;

    /** ЧТО БЫЛО: вся запись до изменения (JSON). */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "snapshot_before", columnDefinition = "json")
    private String snapshotBefore;

    /** ЧТО СТАЛО: вся запись после изменения (JSON). */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "snapshot_after", columnDefinition = "json")
    private String snapshotAfter;

    /** КТО: id пользователя (X-User-Id). */
    @Column(name = "actor_id")
    private Long actorId;

    /** КТО: логин пользователя (X-Username). */
    @Column(name = "actor_name", length = 100)
    private String actorName;

    /** ГДЕ: URL эндпоинта, который вызвал изменение. */
    @Column(name = "endpoint", length = 200)
    private String endpoint;

    /** ЗАЧЕМ: причина / комментарий операции. */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /** КОГДА: время изменения. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}