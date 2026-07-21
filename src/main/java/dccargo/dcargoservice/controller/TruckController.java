package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.DocumentType;
import dccargo.dcargoservice.model.dcargo.EquipmentType;
import dccargo.dcargoservice.model.dcargo.TruckDocument;
import dccargo.dcargoservice.model.dcargo.TruckEquipment;
import dccargo.dcargoservice.model.dcargo.TruckMileage;
import dccargo.dcargoservice.model.dcargo.TruckTire;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.repository.dcargo.DocumentTypeRepository;
import dccargo.dcargoservice.service.dcargo.DocumentTypeService;
import dccargo.dcargoservice.service.dcargo.EquipmentTypeService;
import dccargo.dcargoservice.service.dcargo.TruckDocumentService;
import dccargo.dcargoservice.service.dcargo.TruckEquipmentService;
import dccargo.dcargoservice.service.dcargo.TruckMileageService;
import dccargo.dcargoservice.service.dcargo.TruckService;
import dccargo.dcargoservice.service.dcargo.TruckTireService;
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
    
    private final EquipmentTypeService equipmentTypeService;
    
    private final TruckEquipmentService truckEquipmentService;
    
    private final TruckTireService truckTireService;
    
    private final TruckMileageService truckMileageService;
    
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
    
    @GetMapping("/getAllEquipmentType")
    public ResponseEntity<List<EquipmentType>> getAllEquipmentType() {
        return ResponseEntity.ok(equipmentTypeService.getAll());
    }

    @PostMapping("/createEquipmentType")
    public ResponseEntity<EquipmentType> createEquipmentType(
            @RequestBody EquipmentType equipmentType) {

        log.info("Создание типа оборудования.");

        EquipmentType savedEquipmentType =
                equipmentTypeService.create(equipmentType);

        return ResponseEntity.ok(savedEquipmentType);
    }
    
    /**
     * Отдаёт всё оборудование по ID машины.
     *
     * @param id ID транспортного средства
     * @return оборудование транспортного средства
     */
    @GetMapping("/getTruckEquipment/{id}")
    public ResponseEntity<List<TruckEquipment>> getTruckEquipment(
            @PathVariable Long id) {

        List<TruckEquipment> truckEquipment =
                truckEquipmentService.getByTruckId(id);

        return ResponseEntity.ok(truckEquipment);
    }


    /**
     * Создание оборудования транспортного средства.
     *
     * @param truckEquipment оборудование
     * @return сохранённое оборудование
     */
    @PostMapping("/createNewTruckEquipment")
    public ResponseEntity<TruckEquipment> createTruckEquipment(
            @RequestBody TruckEquipment truckEquipment) {

        log.info(
                "Создание оборудования. Госномер: {}, тип оборудования: {}",
                truckEquipment.getRegistrationNumber(),
                truckEquipment.getEquipmentTypeId()
        );

        TruckEquipment savedTruckEquipment =
                truckEquipmentService.create(truckEquipment);

        return ResponseEntity.ok(savedTruckEquipment);
    }


    /**
     * Обновление оборудования транспортного средства.
     * Важно, чтобы передавалось поле id.
     *
     * @param truckEquipment оборудование
     * @return обновлённое оборудование
     */
    @PostMapping("/updateTruckEquipment")
    public ResponseEntity<TruckEquipment> updateTruckEquipment(
            @RequestBody TruckEquipment truckEquipment) {

        TruckEquipment updatedTruckEquipment =
                truckEquipmentService.update(truckEquipment);

        log.info(
                "Обновление оборудования. ID: {}, Госномер: {}",
                truckEquipment.getId(),
                truckEquipment.getRegistrationNumber()
        );

        return ResponseEntity.ok(updatedTruckEquipment);
    }
    
    /*
     * =============КОЛЁСА=================
     */
    /**
     * Получить все шины
     */
    @GetMapping("/getAllTruckTire")
    public List<TruckTire> getAllTruckTire() {
        return truckTireService.getAllTruckTire();
    }
    
    /**
     * Получить шины транспортного средства
     */
    @GetMapping("/getTruckTire/{truckId}")
    public List<TruckTire> getTruckTire(@PathVariable Long truckId) {

        return truckTireService.getByTruckId(truckId);
    }
    
    /**
     * Создать новую шину
     */
    @PostMapping("/createNewTruckTire")
    public TruckTire createNewTruckTire(@RequestBody TruckTire truckTire) {

        return truckTireService.create(truckTire);
    }
    
    /**
     * Обновить данные шины
     */
    @PostMapping("/updateTruckTire")
    public TruckTire updateTruckTire(@RequestBody TruckTire truckTire) {

        return truckTireService.update(truckTire);
    }
    
    /*
     * =============ПРОБЕГ=================
     */

    /**
     * Получить все записи пробега.
     */
    @GetMapping("/getAllTruckMileage")
    public List<TruckMileage> getAllTruckMileage() {
    	//TODO потом отключить
        return truckMileageService.getAllTruckMileage();
    }

    /**
     * Получить всю историю пробега автомобиля.
     */
    @GetMapping("/getTruckMileage/{truckId}")
    public List<TruckMileage> getTruckMileage(
            @PathVariable Long truckId) {

        return truckMileageService.getByTruckId(truckId);
    }

    /**
     * Получить последний зафиксированный пробег автомобиля.
     */
    @GetMapping("/getLastTruckMileage/{truckId}")
    public TruckMileage getLastTruckMileage(
            @PathVariable Long truckId) {

        return truckMileageService.getLastMileage(truckId);
    }

    /**
     * Создать новую запись пробега.
     */
    @PostMapping("/createNewTruckMileage")
    public TruckMileage createNewTruckMileage(
            @RequestBody TruckMileage truckMileage) {

        return truckMileageService.create(truckMileage);
    }
    
}
