package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.Order;
import dccargo.dcargoservice.model.dcargo.OrderTruck;
import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.repository.dcargo.OrderTruckRepository;
import dccargo.dcargoservice.service.dcargo.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderTruckRepository orderTruckRepository;



    @GetMapping("/getOrderById")
    public ResponseEntity<Order> getOrderById(@RequestParam Long idOrder){
        Order order = orderService.getOrderById(idOrder);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getOrdersByDeliveryDateAndTypeSklad")
    public ResponseEntity<List<Order>> getOrdersByDeliveryDateAndTypeSklad(@RequestParam LocalDate deliveryDate, String typeSklad){
        List<Order> orderList= orderService.getOrdersByDeliveryDateAndTypeSklad(deliveryDate, typeSklad);
        return ResponseEntity.ok(orderList);
    }

    @PostMapping("/updateOrder")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {

        Order savedOrder = orderService.updateOrder(order);

        return ResponseEntity.ok(savedOrder);
    }

    @PostMapping("/createExternalOrder")
    public ResponseEntity<Order> createExternalOrder(@RequestBody Order order) {

        Order savedOrder = orderService.createExternalOrder(order);

        return ResponseEntity.ok(savedOrder);
    }

    @PostMapping("/createOrderTruckAssigment")
    public ResponseEntity<Order> createExternalOrder(@RequestBody Long idTruck,@RequestBody Long idOrder) {

        OrderTruck savedOrderTruck = orderService.createExternalOrder(order);

        return ResponseEntity.ok(savedOrder);
    }

    @PostMapping("/updateOrderTruckAssigment")
    public ResponseEntity<Order> createExternalOrder(@RequestBody Long idTruck,@RequestBody Long idOrder) {

        OrderTruck savedOrderTruck = orderService.createExternalOrder(order);

        return ResponseEntity.ok(savedOrder);
    }

    @PostMapping("/deleteOrderTruckAssigment")
    public ResponseEntity<Order> createExternalOrder(@RequestBody Long idTruck,@RequestBody Long idOrder) {

        OrderTruck savedOrderTruck = orderService.createExternalOrder(order);

        return ResponseEntity.ok(savedOrder);
    }


}
