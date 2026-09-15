package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDateTime;

import dccargo.dcargoservice.enums.FuelCardStatus;
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
@Table(name = "fuel_card")
public class FuelCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "fuel_type")
    private String fuelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private FuelCardStatus status;

    @Column(name = "comment", length = 500)
    private String comment;

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

        if (status == null) {
            status = FuelCardStatus.ACTIVE;
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