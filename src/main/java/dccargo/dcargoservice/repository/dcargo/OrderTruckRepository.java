package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.enums.OrderTruckAssigmentStatus;
import dccargo.dcargoservice.model.dcargo.OrderTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTruckRepository extends JpaRepository<OrderTruck,Long> {

    List<OrderTruck> findByIdOrderAndStatus(Long idOrder, OrderTruckAssigmentStatus status);

}
