package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.OrderTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTruckRepository extends JpaRepository<OrderTruck,Long> {

}
