package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.DocumentType;
import dccargo.dcargoservice.model.dcargo.TechnicalInspection;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.repository.dcargo.DocumentTypeRepository;
import dccargo.dcargoservice.service.dcargo.DocumentTypeService;
import dccargo.dcargoservice.service.dcargo.TechnicalInspectionService;
import dccargo.dcargoservice.service.dcargo.TruckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class TruckController {

    private final TruckService truckService;
    
    private final TechnicalInspectionService technicalInspectionService;
 
    private final DocumentTypeService documentTypeService;
    
    @GetMapping("/echo")
    public ResponseEntity<String> echo() {
        return ResponseEntity.ok("echo");
    }
        
    @GetMapping("/getAllTruck")
    public ResponseEntity<List<Truck>> getAllTruck() {
    	List<Truck> trucks = truckService.getAllTruck();
        return ResponseEntity.ok(trucks);
    }
    
    /**
     * Метод сохранения создания одной машины.
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
    
    
    @GetMapping("/getTechnicalInspection/{id}")
    public ResponseEntity<List<TechnicalInspection>> getTechnicalInspection(@PathVariable Long id) {
    	List<TechnicalInspection> technicalInspections = technicalInspectionService.getByTruckId(id);
        return ResponseEntity.ok(technicalInspections);
    }
    
    @PostMapping("/createNewTechnicalInspection")
    public ResponseEntity<TechnicalInspection> createTechnicalInspection(@RequestBody TechnicalInspection technicalInspection) {
        log.info("Создание ТО. Госномер: {}", technicalInspection.getRegistrationNumber());
        TechnicalInspection savedTechnicalInspection = technicalInspectionService.create(technicalInspection);
        return ResponseEntity.ok(savedTechnicalInspection);
    }
    
    
    /**
     * Важно чтобы передавалось поле id
     * @param technicalInspection
     * @return
     */
    @PostMapping("/updateTechnicalInspection")
    public ResponseEntity<TechnicalInspection> updateTechnicalInspection(@RequestBody TechnicalInspection technicalInspection) {

    	TechnicalInspection updatedTechnicalInspection = technicalInspectionService.update(technicalInspection);
        log.info("Обновление ТО. Госномер: {}",
        		technicalInspection.getRegistrationNumber());

        return ResponseEntity.ok(updatedTechnicalInspection);
    }
    
    @GetMapping("/getAllDocumentType")
    public ResponseEntity<List<DocumentType>> getAllDocumentType() {    	
        return ResponseEntity.ok(documentTypeService.getAll());
    }
    
    @PostMapping("/createDocumentType")
    public ResponseEntity<DocumentType> createDocumentType (@RequestBody DocumentType documentType) {
        log.info("Создание Типа документа.");
        DocumentType savedDocumentType = documentTypeService.create(documentType);
        return ResponseEntity.ok(savedDocumentType);
    }

}
