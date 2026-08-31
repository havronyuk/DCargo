package dccargo.dcargoservice.model.dcargo;

import dccargo.dcargoservice.enums.OrderTruckAssigmentStatus;
import dccargo.dcargoservice.enums.RouteSheetStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "route_sheet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_route_sheet")
    private Long idRouteSheet;

    @Column(name = "id_truck_user_assignment")
    private Long idTruckUserAssignment;

    @Column(name = "id_order")
    private Long idOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_print_time")
    private LocalDateTime lastPrintTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RouteSheetStatus status;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (status == null) {
            status = RouteSheetStatus.ACTIVE;
        }

        if (createdAt == null) {
            createdAt = now;
        }
    }

    @Column(name = "ref_work_time")
    private Double refWorkTime;

    @Column(name = "vebast_work_time")
    private Double vebastWorkTime;

    @Column(name = "start_odometer_value")
    private Integer startOdometerValue;

    @Column(name = "end_odometer_value")
    private Integer endOdometerValue;


}