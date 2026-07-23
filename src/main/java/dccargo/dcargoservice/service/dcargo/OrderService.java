package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.model.dcargo.Order;
import dccargo.dcargoservice.repository.dcargo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    public Order getOrderById(Long idOrder) {
        return orderRepository.getByIdOrder(idOrder);
    }


    public Order createOrderFromShipment(Order order) {
        return null;
    }
}
