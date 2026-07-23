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
        private Long idOrderPoint;

        /**
         * Айди связи с ордером
         */
        @Column(name = "id_order")
        private Long idOrder;

        /**
         * Айди для связи с точкой из УД
         */
        @Column(name = "id_vehicle_session_point")
        private Integer idVehicleSessionPoint;

        /**
         * Айди магазина
         */
        @Column(name = "warehouse_id")
        private Integer warehouseId;

        /**
         * Установленный тоннаж в кг
         */
        @Column(name = "tonnage")
        private Double tonnage;

        /**
         * Паллеты
         */
        @Column(name = "palls")
        private Double palls;

        /**
         * Нужна ли чистка
         */
        @Column(name = "cleanings")
        private Boolean cleanings;

        /**
         * Порядковый номер
         */
        @Column(name = "route_order")
        private Integer routeOrder;

        /**
         * Будет ли чистится по факту
         */
        @Column(name = "is_cleaned")
        private Boolean isCleaned;

        /**
         * Интервал чисток
         */
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

        /**
         * Адрес магазина
         */
        @Column(name = "address", length = 255)
        private String address;

    }