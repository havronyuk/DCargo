package dccargo.dcargoservice.model.dcargo;

import jakarta.persistence.Entity;
import lombok.*;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refueling")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refueling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_refueling")
    private Long idRefueling;

    @Column(name = "id_route_sheet")
    private Long idRouteSheet;

    @Column(name = "fuel_amount")
    private Double fuelAmount;

    @Column(name = "fuel_grade")
    private String fuelGrade;

}
