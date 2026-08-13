package dccargo.dcargoservice.model.dcargo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dccargo.dcargoservice.enums.OrderTruckAssigmentStatus;
import dccargo.dcargoservice.enums.TruckUserAssignmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_truck")
public class OrderTruck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "id_order")
    private Long idOrder;


    @Column(name = "id_truck")
    private Long idTruck;

    @Column(name = "id_truck_user_assigment")
    private Long idTruckUserAssigment;

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "from_system")
    private String fromSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderTruckAssigmentStatus status;


    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (status == null) {
            status = OrderTruckAssigmentStatus.ACTIVE;
        }

        if (createdAt == null) {
            createdAt = now;
        }
    }

}