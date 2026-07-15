package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.service.dcargo.TruckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class TruckController {

    private final TruckService truckService;
    
    @GetMapping("/echo")
    public ResponseEntity<String> echo() {
        log.info("Echo request");
        log.error("error");
        System.out.println("test");
        return ResponseEntity.ok("echo");
    }

}
