package dccargo.dcargoservice.model.dcargo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dccargo.dcargoservice.enums.TirePosition;
import dccargo.dcargoservice.enums.TireSeasonType;
import dccargo.dcargoservice.enums.TireStatus;
import dccargo.dcargoservice.enums.TireType;
import dccargo.dcargoservice.enums.TruckEquipmentStatus;
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

@Data
@Entity
@Getter
@Setter
@Table(name = "truck_tire")
public class TruckTire {

	/** Уникальный идентификатор записи */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID транспортного средства */
    private Long truckId;

    /** Государственный номер ТС (заполняется автоматически) */
    private String registrationNumber;

    /** Инвентарный номер шины */
    private String inventoryNumber;

    /** Серийный номер производителя */
    private String serialNumber;

    /** Производитель (Michelin, Bridgestone и т.д.) */
    private String brand;

    /** Модель шины */
    private String model;

    /** Тип шины (рулевая, ведущая, прицепная...) */
    @Enumerated(EnumType.STRING)
    private TireType tireType;

    /** Сезонность */
    @Enumerated(EnumType.STRING)
    private TireSeasonType seasonType;

    /** Ширина профиля (например 315) */
    private Integer width;

    /** Высота профиля (например 70) */
    private Integer profile;

    /** Диаметр шины в дюймах, например 22.5 */
    @Column(precision = 5, scale = 2)
    private BigDecimal diameter;

    /** Дата производства */
    private LocalDateTime manufactureDate;

    /** Дата покупки */
    private LocalDateTime purchaseDate;

    /** Дата установки на автомобиль */
    private LocalDateTime installationDate;

    /** Дата снятия */
    private LocalDateTime removalDate;

    /** Номер оси */
    private Integer axleNumber;

    /** Позиция на оси */
    @Enumerated(EnumType.STRING)
    private TirePosition position;

    /** ID записи пробега при установке */
    private Long mileageStartId;

    /** Пробег автомобиля при установке */
    private Integer mileageStartValue;

    /** ID записи пробега при снятии */
    private Long mileageEndId;

    /** Пробег автомобиля при снятии */
    private Integer mileageEndValue;

    /** Глубина протектора при установке (мм) */
    @Column(precision = 5, scale = 2)
    private BigDecimal treadDepthStart;

    /** Текущая глубина протектора (мм) */
    @Column(precision = 5, scale = 2)
    private BigDecimal treadDepthCurrent;

    /** Текущее давление (бар) */
    @Column(precision = 5, scale = 2)
    private BigDecimal pressure;

    /** Статус шины */
    @Enumerated(EnumType.STRING)
    private TireStatus status;

    /** Комментарий */
    @Column(length = 2000)
    private String comment;

    /** Дата создания записи */
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Дата последнего изменения */
    @Column(insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
