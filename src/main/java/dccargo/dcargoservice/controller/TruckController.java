package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.TechnicalInspection;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.service.dcargo.TechnicalInspectionService;
import dccargo.dcargoservice.service.dcargo.TruckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class TruckController {

    private final TruckService truckService;
    
    private final TechnicalInspectionService technicalInspectionService;
    
    @GetMapping("/echo")
    public ResponseEntity<String> echo() {
        return ResponseEntity.ok("echo");
    }
    
    /**
     * Метод сохранения создания одной машины.
     * 
     * 
     * @param truck
     * @return
     */
    @PostMapping("/createNewTruck")
    public ResponseEntity<Truck> create(@RequestBody Truck truck) {
        log.info("Создание ТС. Госномер: {}", truck.getRegistrationNumber());
        Truck savedTruck = truckService.create(truck);
        return ResponseEntity.ok(savedTruck);
    }
    
    
    /**
     * Важно чтобы передавалось поле id
     * @param truck
     * @return
     */
    @PostMapping("/updateTruck")
    public ResponseEntity<Truck> update(@RequestBody Truck truck) {

        Truck updatedTruck = truckService.update(truck);
        log.info("Обновление ТС. ID: {}, Госномер: {}",
                truck.getId(),
                truck.getRegistrationNumber());

        return ResponseEntity.ok(updatedTruck);
    }
    
    @PostMapping("/createNewTechnicalInspection")
    public ResponseEntity<TechnicalInspection> createTechnicalInspection(@RequestBody TechnicalInspection technicalInspection) {
        log.info("Создание ТО. Госномер: {}", technicalInspection.getRegistrationNumber());
        TechnicalInspection savedTechnicalInspection = technicalInspectionService.create(technicalInspection);
        return ResponseEntity.ok(savedTechnicalInspection);
    }

}
