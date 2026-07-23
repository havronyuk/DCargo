package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import dccargo.dcargoservice.enums.DriverScheduleExceptionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "driver_schedule_exception")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverScheduleException {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Водитель, для которого установлено исключение.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Автомобиль, к которому относится изменение.
     */
    @Column(name = "truck_id")
    private Long truckId;

    /**
     * Дата, на которую действует исключение.
     */
    @Column(name = "exception_date", nullable = false)
    private LocalDate exceptionDate;

    /**
     * Причина или тип изменения рабочего графика.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "exception_type", nullable = false, length = 30)
    private DriverScheduleExceptionType exceptionType;

    /**
     * Водитель, который выходит на замену.
     *
     * Заполняется при exceptionType = REPLACEMENT.
     */
    @Column(name = "replacement_user_id")
    private Long replacementUserId;

    @Column(name = "comment", length = 500)
    private String comment;

    /**
     * Пользователь, который внёс изменение.
     */
    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
