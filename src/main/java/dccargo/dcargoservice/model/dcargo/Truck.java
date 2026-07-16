package dccargo.dcargoservice.model.dcargo;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dccargo.dcargoservice.enums.TruckStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "truck")
public class Truck {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "internal_id")
    private String internalId;

    @Column(name = "internal_number", nullable = false)
    private String internalNumber;

    @Column(name = "garage_number")
    private String garageNumber;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Column(name = "vin")
    private String vin;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "manufacture_year")
    private Integer manufactureYear;

    @Column(name = "truck_type")
    private String truckType;

    @Column(name = "length_mm")
    private Integer lengthMm;

    @Column(name = "width_mm")
    private Integer widthMm;

    @Column(name = "height_mm")
    private Integer heightMm;

    @Column(name = "volume_m3", precision = 10, scale = 2)
    private BigDecimal volumeM3;

    @Column(name = "max_weight_kg", precision = 10, scale = 2)
    private BigDecimal maxWeightKg;

    @Column(name = "max_euro_pallets")
    private Integer maxEuroPallets;

    @Column(name = "max_fin_pallets")
    private Integer maxFinPallets;

    @Column(name = "max_rollboxes")
    private Integer maxRollboxes;

    @Column(name = "commissioning_date")
    private LocalDate commissioningDate;

    @Column(name = "decommissioning_date")
    private LocalDate decommissioningDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TruckStatus status;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
