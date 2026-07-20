package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDateTime;

import dccargo.dcargoservice.enums.TruckEquipmentStatus;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "truck_equipment")
public class TruckEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * ID транспортного средства.
     */
    @Column(name = "truck_id", nullable = false)
    private Long truckId;

    /**
     * Государственный номер автомобиля.
     */
    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    /**
     * ID типа оборудования.
     */
    @Column(name = "equipment_type_id", nullable = false)
    private Long equipmentTypeId;

    /**
     * Название типа оборудования.
     */
    @Column(name = "equipment_type_name", nullable = false)
    private String equipmentTypeName;

    /**
     * Инвентарный номер.
     */
    @Column(name = "inventory_number")
    private String inventoryNumber;

    /**
     * Производитель.
     */
    @Column(name = "brand")
    private String brand;

    /**
     * Модель.
     */
    @Column(name = "model")
    private String model;

    /**
     * Серийный номер.
     */
    @Column(name = "serial_number")
    private String serialNumber;

    /**
     * Дата установки.
     */
    @Column(name = "installation_date", nullable = false)
    private LocalDateTime installationDate;

    /**
     * Дата снятия.
     */
    @Column(name = "removal_date")
    private LocalDateTime removalDate;

    /**
     * Наработка оборудования в часах.
     */
    @Column(name = "working_hours")
    private Long workingHours;

    /**
     * ID записи пробега при установке.
     */
    @Column(name = "mileage_start_id")
    private Long mileageStartId;

    /**
     * Пробег при установке.
     */
    @Column(name = "mileage_start_value")
    private Long mileageStartValue;

    /**
     * ID записи пробега при снятии.
     */
    @Column(name = "mileage_end_id")
    private Long mileageEndId;

    /**
     * Пробег при снятии.
     */
    @Column(name = "mileage_end_value")
    private Long mileageEndValue;

    /**
     * Статус оборудования.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TruckEquipmentStatus status;

    /**
     * Комментарий.
     */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /**
     * Дата создания.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата изменения.
     */
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}