package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "technical_inspection")
public class TechnicalInspection {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * ID транспортного средства.
     * Связь с Truck храним вручную, без @ManyToOne.
     */
    @Column(name = "truck_id", nullable = false)
    private Long truckId;

    /**
     * Государственный номер автомобиля.
     * Хранится отдельно для упрощения поиска и отображения.
     */
    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    /**
     * Дата и время прохождения технического осмотра.
     */
    @Column(name = "inspection_date", nullable = false)
    private LocalDateTime inspectionDate;

    /**
     * Дата окончания действия технического осмотра.
     */
    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    /**
     * ID записи пробега на момент прохождения ТО.
     */
    @Column(name = "mileage_start_id")
    private Long mileageStartId;

    /**
     * ID записи пробега на момент окончания действия ТО.
     */
    @Column(name = "mileage_end_id")
    private Long mileageEndId;

    /**
     * Номер документа технического осмотра.
     */
    @Column(name = "document_number")
    private String documentNumber;

    /**
     * Статус технического осмотра.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TechnicalInspectionStatus status = TechnicalInspectionStatus.ACTIVE;

    /**
     * Дополнительный комментарий.
     */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /**
     * Дата создания записи.
     * Значение устанавливает база данных.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата последнего изменения записи.
     * Значение устанавливает база данных.
     */
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
