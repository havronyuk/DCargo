package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.DocumentType;
import dccargo.dcargoservice.model.dcargo.TruckDocument;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.repository.dcargo.DocumentTypeRepository;
import dccargo.dcargoservice.service.dcargo.DocumentTypeService;
import dccargo.dcargoservice.service.dcargo.TruckDocumentService;
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
    
    private final TruckDocumentService truckDocumentService;
 
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
    
    
    /**
     * Отдаёт все документа по id машины
     * @param id
     * @return
     */
    @GetMapping("/getTruckDocument/{id}")
    public ResponseEntity<List<TruckDocument>> getTruckDocument(@PathVariable Long id) {
    	List<TruckDocument> truckDocument = truckDocumentService.getByTruckId(id);
        return ResponseEntity.ok(truckDocument);
    }
    
    @PostMapping("/createNewTruckDocument")
    public ResponseEntity<TruckDocument> createTruckDocument(@RequestBody TruckDocument truckDocument) {
        log.info("Создание Документа. Госномер: {}", truckDocument.getRegistrationNumber());
        TruckDocument savedTechnicalInspection = truckDocumentService.create(truckDocument);
        return ResponseEntity.ok(savedTechnicalInspection);
    }
    
    
    /**
     * Важно чтобы передавалось поле id
     * @param truckDocument
     * @return
     */
    @PostMapping("/updateTruckDocument")
    public ResponseEntity<TruckDocument> updateTruckDocument(@RequestBody TruckDocument truckDocument) {

    	TruckDocument updatedTechnicalInspection = truckDocumentService.update(truckDocument);
        log.info("Обновление Документа. Госномер: {}",
        		truckDocument.getRegistrationNumber());

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
