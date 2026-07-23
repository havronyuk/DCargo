package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "driver_work_schedule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverWorkSchedule {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID водителя.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * ID автомобиля.
     * Может быть null, если график пока не привязан
     * к конкретному автомобилю.
     */
    @Column(name = "truck_id")
    private Long truckId;

    /**
     * ID шаблона рабочего графика.
     */
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    /**
     * Дата, относительно которой рассчитывается цикл.
     */
    @Column(name = "cycle_start_date", nullable = false)
    private LocalDate cycleStartDate;

    /**
     * Смещение водителя внутри цикла.
     *
     * Для двух водителей при графике 2/2:
     * первый водитель — 0;
     * второй водитель — 2.
     */
    @Column(name = "cycle_offset", nullable = false)
    private Integer cycleOffset;

    /**
     * Дата начала действия назначения.
     */
    @Column(name = "date_from", nullable = false)
    private LocalDate dateFrom;

    /**
     * Дата окончания действия назначения.
     * Null означает, что назначение бессрочное.
     */
    @Column(name = "date_to")
    private LocalDate dateTo;

    /**
     * ACTIVE или INACTIVE.
     *
     * Пока оставляем String, чтобы не создавать
     * дополнительный enum.
     */
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (cycleOffset == null) {
            cycleOffset = 0;
        }

        if (dateFrom == null) {
            dateFrom = LocalDate.now();
        }

        if (status == null) {
            status = "ACTIVE";
        }

        if (createdAt == null) {
            createdAt = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
