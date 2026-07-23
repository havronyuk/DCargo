package dccargo.dcargoservice.controller;

import dccargo.dcargoservice.model.dcargo.Order;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.service.dcargo.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
@RequestMapping("/internal")
public class InternalController {

    private final OrderService orderService;

    @PostMapping("/createOrderFromShipment")
    public ResponseEntity<Order> createOrderFromShipment(@RequestBody Order order) {
        Order savedOrder = orderService.createOrderFromShipment(order);

        return ResponseEntity.ok(savedOrder);
    }


}
