package dccargo.dcargoservice.model.dcargo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long idOrder;

    /**
     * Айди для связки из уд
     */
    @Column(name = "id_vehicle_session")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long idVehicleSession;

    /**
     * Название перевозчика
     */
    @Column(name = "firm_name", length = 255)
    private String firmName;

    /**
     * Максимальное кол-во паллет в авто
     */
    @Column(name = "max_pall")
    private Double maxPall;

    /**
     * Максимальный вес авто в кг
     */
    @Column(name = "max_weigth")
    private Integer maxWeigth;

    /**
     * Паллеты из рассчетов SL
     */
    @Column(name = "target_pall")
    private Double targetPall;

    /**
     * Вес из рассчетов SL
     */
    @Column(name = "target_weigth")
    private Double targetWeigth;

    /**
     * Тип склада с которого будет отгружаться авто
     */
    @Column(name = "type_sklad", length = 45)
    private String typeSklad;

    /**
     * Плановая дата доставки
     */
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    /**
     * Плановая дата отгрузки
     */
    @Column(name = "shipment_plan_date")
    private LocalDateTime shipmentPlanDate;

    /**
     * Статус
     */
    @Column(name = "status")
    private Integer status;

    /**
     * Кросс док
     */
    @Column(name = "is_cross_dock")
    private Boolean isCrossDock;

    /**
     * Кэп
     */
    @Column(name = "is_kep")
    private Boolean isKep;

    /**
     * Номер телефона фирмы перевозчика
     */
    @Column(name = "firm_phone_number", columnDefinition = "TEXT")
    private String firmPhoneNumber;

    /**
     * Дата создания записи
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Кем создана запись
     */
    @Column(name = "created_by", length = 255)
    private String createdBy;

    /**
     * Затраты
     */
    @Column(name = "cost")
    private Double cost;

    /**
     * Внутренние затраты
     */
    @Column(name = "cost_internal")
    private Double costInternal;

    /**
     * Валюта
     */
    @Column(name = "currency", length = 45)
    private String currency;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_order", referencedColumnName = "id_order")
    private List<OrderPoint> orderPoints;


    @Override
    public String toString() {
        return "Order{" +
                "idOrder=" + idOrder +
                ", idVehicleSession=" + idVehicleSession +
                ", firmName='" + firmName + '\'' +
                ", maxPall=" + maxPall +
                ", maxWeigth=" + maxWeigth +
                ", targetPall=" + targetPall +
                ", targetWeigth=" + targetWeigth +
                ", typeSklad='" + typeSklad + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", shipmentPlanDate=" + shipmentPlanDate +
                ", status=" + status +
                ", isCrossDock=" + isCrossDock +
                ", isKep=" + isKep +
                ", firmPhoneNumber='" + firmPhoneNumber + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", cost=" + cost +
                ", costInternal=" + costInternal +
                ", currency='" + currency + '\'' +
                ", orderPoints=" + orderPoints +
                '}';
    }
}
