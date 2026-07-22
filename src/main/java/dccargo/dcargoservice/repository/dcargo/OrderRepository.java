package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Order getByIdOrder(Long idOrder);

}
