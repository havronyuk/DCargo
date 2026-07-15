package dccargo.dcargoservice.repository.dcargo;


import dccargo.dcargoservice.model.dcargo.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Integer> {
}
