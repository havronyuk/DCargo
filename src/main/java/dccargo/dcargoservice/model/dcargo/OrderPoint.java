package dccargo.dcargoservice.model.dcargo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_point")
public class OrderPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_point")
    private Integer idOrderPoint;

    /**
     * Айди связи с ордером
     */
    @Column(name = "id_order")
    private Integer idOrder;

    /**
     * Айди для связи с точкой из УД
     */
    @Column(name = "id_vehicle_session_point")
    private Integer idVehicleSessionPoint;

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "tonnage")
    private Double tonnage;

    @Column(name = "palls")
    private Double palls;

    @Column(name = "cleanings")
    private Boolean cleanings;

    @Column(name = "route_order")
    private Integer routeOrder;

    @Column(name = "is_cleaned")
    private Boolean isCleaned;

    @Column(name = "cleaning_interval")
    private Integer cleaningInterval;

    /**
     * Широта
     */
    @Column(name = "lat", length = 255)
    private String lat;

    /**
     * Долгота
     */
    @Column(name = "lng", length = 255)
    private String lng;

    @Column(name = "address", length = 255)
    private String address;

}