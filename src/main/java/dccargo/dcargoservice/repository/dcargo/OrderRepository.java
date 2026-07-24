package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Order getByIdOrder(Long idOrder);


    Order getByIdVehicleSession(Long idVehicleSession);

    boolean existsByIdVehicleSession(Long idVehicleSession);

    @EntityGraph(attributePaths = {
            "orderPoints",
    })
    List<Order> findAllByDeliveryDateAndTypeSklad(LocalDate deliveryDate, String typeSklad);


}
