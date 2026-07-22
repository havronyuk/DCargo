package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDateTime;

import dccargo.dcargoservice.enums.TruckUserAssignmentStatus;
import dccargo.dcargoservice.enums.TruckUserAssignmentType;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "truck_user_assignment")
public class TruckUserAssignment {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id")
	    private Long id;

	    /**
	     * ID автомобиля.
	     * В БД внешний ключ на truck.id.
	     */
	    @Column(name = "truck_id", nullable = false)
	    private Long truckId;

	    /**
	     * ID закреплённого пользователя.
	     * В БД внешний ключ на user.id_user.
	     */
	    @Column(name = "user_id", nullable = false)
	    private Long userId;

	    /**
	     * Тип закрепления:
	     * PERMANENT — постоянное;
	     * TEMPORARY — временное;
	     * ACTUAL — фактическое.
	     */
	    @Enumerated(EnumType.STRING)
	    @Column(name = "assignment_type", nullable = false, length = 30)
	    private TruckUserAssignmentType assignmentType;

	    /**
	     * Статус закрепления:
	     * ACTIVE, COMPLETED, EXPIRED, CANCELLED.
	     */
	    @Enumerated(EnumType.STRING)
	    @Column(name = "status", nullable = false, length = 30)
	    private TruckUserAssignmentStatus status;

	    /**
	     * Дата начала закрепления.
	     */
	    @Column(name = "date_from", nullable = false)
	    private LocalDateTime dateFrom;

	    /**
	     * Дата окончания закрепления.
	     * Для действующего закрепления может быть null.
	     */
	    @Column(name = "date_to")
	    private LocalDateTime dateTo;

	    @Column(name = "comment", length = 500)
	    private String comment;

	    @Column(name = "created_at", nullable = false)
	    private LocalDateTime createdAt;

	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;

	    /**
	     * ID пользователя, создавшего запись.
	     * В БД внешний ключ на user.id_user.
	     */
	    @Column(name = "created_by_user_id")
	    private Long createdByUserId;

	    @PrePersist
	    public void prePersist() {
	        LocalDateTime now = LocalDateTime.now();

	        if (dateFrom == null) {
	            dateFrom = now;
	        }

	        if (status == null) {
	            status = TruckUserAssignmentStatus.ACTIVE;
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
