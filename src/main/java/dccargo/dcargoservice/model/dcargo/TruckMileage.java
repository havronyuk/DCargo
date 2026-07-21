package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDateTime;

import dccargo.dcargoservice.enums.MileageSource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "truck_mileage")
public class TruckMileage {
	
	 /** Уникальный идентификатор записи */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID транспортного средства */
    private Long truckId;

    /** Государственный номер транспортного средства */
    private String registrationNumber;

    /** Значение одометра (км) */
    private Integer mileage;

    /** Дата и время фиксации пробега */
    private LocalDateTime mileageDate;

    /** Источник получения пробега */
    @Enumerated(EnumType.STRING)
    private MileageSource source;

    /**
     * Связанные объекты в JSON-формате.
     *
     * Пример:
     * {
     *   "technicalInspectionIds":[15],
     *   "truckTireIds":[10,11],
     *   "truckEquipmentIds":[5],
     *   "truckServiceIds":[8]
     * }
     */
    @Column(columnDefinition = "json")
    private String relatedEntities;

    /** ID пользователя, создавшего запись */
    private Long createdByUserId;

    /** Комментарий */
    @Column(length = 2000)
    private String comment;

    /** Дата создания записи */
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

}
