package dccargo.dcargoservice.controller;

import dccargo.dcargoservice.dto.dcargo.TruckAndUserDTO;
import dccargo.dcargoservice.model.dcargo.Order;
import dccargo.dcargoservice.model.dcargo.OrderTruck;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.service.dcargo.ExternalRequestService;
import dccargo.dcargoservice.service.dcargo.OrderService;
import dccargo.dcargoservice.service.dcargo.OrderTruckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
@RequestMapping("/internal")
public class InternalController {

    private final OrderService orderService;
    private final ExternalRequestService externalRequestService;
    private final OrderTruckService orderTruckService;

    @PostMapping("/createOrderFromShipment")
    public ResponseEntity<Object> createOrderFromShipment(@RequestBody Order order) {
        try {
            Order savedOrder = orderService.createOrderFromShipment(order);

            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/getTruckUsers")
    public ResponseEntity<List<TruckAndUserDTO>> getTruckUsers(
            @RequestParam LocalDate workDate
    ) {
        try {
            List<TruckAndUserDTO> result =
                    externalRequestService.getTruckUsersByWorkDate(workDate);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


//    @PostMapping("/assignTruckUserToOrder")
//    public ResponseEntity<Object> assignTruckUserToOrder() {
//        try {
//            OrderTruck savedAssigment = orderTruckService.assignTruckUserToOrder();
//
//            return ResponseEntity.ok(savedAssigment);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }


}
