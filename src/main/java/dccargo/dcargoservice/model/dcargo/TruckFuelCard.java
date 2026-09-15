package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "truck_fuel_card")
public class TruckFuelCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_truck", nullable = false)
    private Long idTruck;

    @Column(name = "id_fuel_card", nullable = false)
    private Long idFuelCard;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "from_system")
    private String fromSystem;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "created_by_user_name")
    private String createdByUserName;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (isActive == null) {
            isActive = true;
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