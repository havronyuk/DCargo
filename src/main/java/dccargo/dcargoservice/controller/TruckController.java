package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.service.dcargo.TruckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class TruckController {

    private final TruckService truckService;

}
