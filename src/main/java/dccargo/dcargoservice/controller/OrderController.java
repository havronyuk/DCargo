package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.Order;
import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.service.dcargo.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;



    @GetMapping("/getOrderById")
    public ResponseEntity<Order> getOrderById(@RequestParam Long idOrder){
        Order order = orderService.getOrderById(idOrder);
        return ResponseEntity.ok(order);
    }




}
