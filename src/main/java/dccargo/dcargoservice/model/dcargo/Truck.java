package dccargo.dcargoservice.model.dcargo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "truck")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_truck")
    private Integer idTruck;


}
