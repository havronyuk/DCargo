package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.dto.dcargo.TruckDTO;
import dccargo.dcargoservice.enums.TruckUserAssignmentType;
import dccargo.dcargoservice.model.dcargo.DocumentType;
import dccargo.dcargoservice.model.dcargo.EquipmentType;
import dccargo.dcargoservice.model.dcargo.TruckDocument;
import dccargo.dcargoservice.model.dcargo.TruckEquipment;
import dccargo.dcargoservice.model.dcargo.TruckMileage;
import dccargo.dcargoservice.model.dcargo.TruckTire;
import dccargo.dcargoservice.model.dcargo.TruckUserAssignment;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.repository.dcargo.DocumentTypeRepository;
import dccargo.dcargoservice.service.dcargo.DocumentTypeService;
import dccargo.dcargoservice.service.dcargo.EquipmentTypeService;
import dccargo.dcargoservice.service.dcargo.TruckDTOService;
import dccargo.dcargoservice.service.dcargo.TruckDocumentService;
import dccargo.dcargoservice.service.dcargo.TruckEquipmentService;
import dccargo.dcargoservice.service.dcargo.TruckMileageService;
import dccargo.dcargoservice.service.dcargo.TruckService;
import dccargo.dcargoservice.service.dcargo.TruckTireService;
import dccargo.dcargoservice.service.dcargo.TruckUserAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
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
    
    private final TruckDTOService truckDTOService;
    
    private final TruckUserAssignmentService assignmentService;
    
    @GetMapping("/echo")
    public ResponseEntity<String> echo() {
        return ResponseEntity.ok("echo");
    }
    
    
    /**
     * Получить все машины без связей
     * @return
     */
    @GetMapping("/getAllTruck")
    public ResponseEntity<List<Truck>> getAllTruck() {
    	List<Truck> trucks = truckService.getAllTruck();
        return ResponseEntity.ok(trucks);
    }
    
    /**
     * Получить одну машину со всеми данными.
     * отдаёт связи у которых статус ACTIVE или утсановлено, если колёса
     */
    @GetMapping("/getTruck/{truckId}/full")
    public ResponseEntity<TruckDTO> getById(
            @PathVariable Long truckId
    ) {
        return ResponseEntity.ok(
                truckDTOService.getById(truckId)
        );
    }

    /**
     * Получить все машины со всеми данными.
     * отдаёт связи у которых статус ACTIVE или утсановлено, если колёса
     */
    @GetMapping("/getAllTruck/full")
    public ResponseEntity<List<TruckDTO>> getAll() {
        return ResponseEntity.ok(
                truckDTOService.getAll()
        );
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
    
    /*
     * =============ЗАКРЕПЛЕНИ ЗА ТРАНСПОРТОМ=================
     */
    /**
     * Получить все закрепления.
     */
    @GetMapping("/getAllTruckUserAssignment")
    public ResponseEntity<List<TruckUserAssignment>> getAllTruckUserAssignment() {

        return ResponseEntity.ok(
                assignmentService.getAll()
        );
    }
    
    /**
     * Получить закрепление по id.
     */
    @GetMapping("/getByIdTruckUserAssignment/{id}")
    public ResponseEntity<TruckUserAssignment> getByIdTruckUserAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                assignmentService.getById(id)
        );
    }
    
    /**
     * Получить всю историю закреплений по автомобилю.
     */
    @GetMapping("/getByTruckIdTruckUserAssignment/{truckId}")
    public ResponseEntity<List<TruckUserAssignment>> getByTruckId(
            @PathVariable Long truckId) {

        return ResponseEntity.ok(
                assignmentService.getByTruckId(truckId)
        );
    }
    
    /**
     * Получить всю историю закреплений пользователя.
     */
    @GetMapping("/getByUserIdTruckUserAssignment/{userId}")
    public ResponseEntity<List<TruckUserAssignment>> getByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                assignmentService.getByUserId(userId)
        );
    }
    
    /**
     * Получить активные закрепления автомобиля по типу.
     *
     * Примеры type:
     * PERMANENT
     * TEMPORARY
     * ACTUAL
     */
    @GetMapping("/getActiveByTruckIdAndType/{truckId}/{type}")
    public ResponseEntity<List<TruckUserAssignment>>
    getActiveByTruckIdAndType(
            @PathVariable Long truckId,
            @PathVariable TruckUserAssignmentType type) {

        return ResponseEntity.ok(
                assignmentService.getActiveByTruckIdAndType(
                        truckId,
                        type
                )
        );
    }
    
    /**
     * Получить пользователя, который фактически
     * сейчас находится на автомобиле.
     *
     * Если активного ACTUAL-закрепления нет,
     * вернётся null.
     */
    @GetMapping("/getActualDriverTruckUserAssignment/{truckId}")
    public ResponseEntity<TruckUserAssignment> getActualDriver(
            @PathVariable Long truckId) {

        return ResponseEntity.ok(
                assignmentService.getActualDriver(truckId)
        );
    }
    
    /**
     * Создать новое закрепление.
     */
    @PostMapping("/createTruckUserAssignment")
    public ResponseEntity<TruckUserAssignment> create(
            @RequestBody TruckUserAssignment assignment) {

        return ResponseEntity.ok(
                assignmentService.create(assignment)
        );
    }
    
    /**
     * Частично обновить закрепление.
     *
     * Обязательно передать id.
     * Остальные поля обновляются только если не null.
     */
    @Deprecated
    @PostMapping("/updateTruckUserAssignment")
    public ResponseEntity<String> update(
            @RequestBody TruckUserAssignment assignment) {

//        return ResponseEntity.ok(assignmentService.update(assignment));
    	return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Метод отключён");
    }
    
    /**
     * Штатно завершить закрепление.
     */
    @GetMapping("/completeTruckUserAssignment/{id}")
    public ResponseEntity<TruckUserAssignment> complete(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                assignmentService.complete(id)
        );
    }

    /**
     * Отменить закрепление.
     */
    @GetMapping("/cancelTruckUserAssignment/{id}")
    public ResponseEntity<TruckUserAssignment> cancel(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                assignmentService.cancel(id)
        );
    }
}
