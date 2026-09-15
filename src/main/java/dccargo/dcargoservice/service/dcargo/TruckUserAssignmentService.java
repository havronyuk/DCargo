package dccargo.dcargoservice.service.dcargo;

import dccargo.dcargoservice.audit.Audited;
import java.time.LocalDateTime;
import java.util.List;

import dccargo.dcargoservice.enums.OrderTruckAssigmentStatus;
import dccargo.dcargoservice.enums.RouteSheetStatus;
import dccargo.dcargoservice.model.dcargo.OrderTruck;
import dccargo.dcargoservice.model.dcargo.RouteSheet;
import dccargo.dcargoservice.repository.dcargo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dccargo.dcargoservice.enums.TruckUserAssignmentStatus;
import dccargo.dcargoservice.enums.TruckUserAssignmentType;
import dccargo.dcargoservice.model.dcargo.TruckUserAssignment;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TruckUserAssignmentService {
	
	private final TruckUserAssignmentRepository assignmentRepository;

    private final TruckRepository truckRepository;

    private final UserRepository userRepository;

    private final OrderTruckRepository orderTruckRepository;

    private final RouteSheetRepository routeSheetRepository;
    
    public List<TruckUserAssignment> getAll() {
        return assignmentRepository.findAll();
    }

    public TruckUserAssignment getById(Long id) {

        if (id == null) {
            throw new MainServiceException("Не указан id закрепления");
        }

        return assignmentRepository.findById(id)
                .orElseThrow(() -> new MainServiceException(
                        "Закрепление пользователя за автомобилем не найдено"
                ));
    }

    public List<TruckUserAssignment> getByTruckId(Long truckId) {

        validateTruckId(truckId);

        return assignmentRepository
                .findAllByTruckIdOrderByDateFromDesc(truckId);
    }

    public List<TruckUserAssignment> getByUserId(Long userId) {

        validateUserId(userId);

        return assignmentRepository
                .findAllByUserIdOrderByDateFromDesc(userId);
    }

    public List<TruckUserAssignment> getActiveByTruckIdAndType(
            Long truckId,
            TruckUserAssignmentType assignmentType) {

        validateTruckId(truckId);

        if (assignmentType == null) {
            throw new MainServiceException(
                    "Не указан тип закрепления"
            );
        }

        return assignmentRepository
                .findAllByTruckIdAndAssignmentTypeAndStatusOrderByDateFromDesc(
                        truckId,
                        assignmentType,
                        TruckUserAssignmentStatus.ACTIVE
                );
    }

    /**
     * Получение водителя, который фактически сейчас едет на машине.
     */
    public TruckUserAssignment getActualDriver(Long truckId) {

        validateTruckId(truckId);

        return assignmentRepository
                .findFirstByTruckIdAndAssignmentTypeAndStatusOrderByDateFromDesc(
                        truckId,
                        TruckUserAssignmentType.ACTUAL,
                        TruckUserAssignmentStatus.ACTIVE
                )
                .orElse(null);
    }

    @Transactional
    public TruckUserAssignment create(TruckUserAssignment assignment) {


//        if(assignment.getIsPrimaryDriver() != null){
//            validatePrimaryDriver(assignment);
//
//        }

        validateRequiredFields(assignment);
        validateReferences(assignment);
        validateDates(assignment.getDateFrom(), assignment.getDateTo());

        if (assignment.getStatus() == null) {
            assignment.setStatus(TruckUserAssignmentStatus.ACTIVE);
        }

        /*
         * Запрещаем создавать одинаковое активное закрепление:
         *
         * тот же автомобиль;
         * тот же пользователь;
         * тот же тип;
         * статус ACTIVE.
         */
//        if (assignment.getStatus()
//                == TruckUserAssignmentStatus.ACTIVE
//                && assignmentRepository
//                .existsByTruckIdAndUserIdAndAssignmentTypeAndStatus(
//                        assignment.getTruckId(),
//                        assignment.getUserId(),
//                        assignment.getAssignmentType(),
//                        TruckUserAssignmentStatus.ACTIVE
//                )) {
//
//            throw new MainServiceException(
//                    "Такое активное закрепление уже существует"
//            );
//        }

        /*
         * Если создаётся новое фактическое закрепление,
         * закрываем предыдущего фактического водителя машины.
         */
//        if (assignment.getAssignmentType()
//                == TruckUserAssignmentType.ACTUAL
//                && assignment.getStatus()
//                == TruckUserAssignmentStatus.ACTIVE) {
//
//            completeCurrentActualAssignment(
//                    assignment.getTruckId()
//            );
//        } отключил, т.к. предполагается что можно создавать закрепление на день вперед

        /*
         * createdAt, updatedAt и dateFrom будут заполнены
         * в @PrePersist модели, если они не указаны.
         */

        assignmentRepository.save(assignment);


        if(assignment.getAssignmentType().equals(TruckUserAssignmentType.ACTUAL)){
            boolean isSheetExist = routeSheetRepository.existsByIdTruckUserAssignmentAndStatus(assignment.getId(), RouteSheetStatus.ACTIVE);

            if(!isSheetExist){
                RouteSheet routeSheet = new RouteSheet();
                routeSheet.setIdTruckUserAssignment(assignment.getId());
                routeSheet.setIdTruck(assignment.getTruckId());
                routeSheetRepository.save(routeSheet);
            }

        }



        return assignment;
    }

//    private void validatePrimaryDriver(TruckUserAssignment assignment) {
//        if(assignment.getIsPrimaryDriver()){
//            Boolean isExistPrimary = assignmentRepository.existsByIsPrimaryDriverAndTruckIdAndAssignmentTypeAndStatus(
//                    assignment.getIsPrimaryDriver(),
//                    assignment.getTruckId(),
//                    TruckUserAssignmentType.PERMANENT,
//                    TruckUserAssignmentStatus.ACTIVE
//            );
//            if (isExistPrimary){
//                throw new MainServiceException("Основной водитель на данном авто в указанном промежутке уже существует");
//            }
//        }
//
//    }

    @Audited(operation = "UPDATE_TRUCK_USER_ASSIGNMENT")
    @Transactional
    public TruckUserAssignment update(
            TruckUserAssignment assignment) {

        if (assignment.getId() == null) {
            throw new MainServiceException(
                    "Отсутствует id в запросе"
            );
        }

        TruckUserAssignment dbAssignment =
                assignmentRepository.findById(assignment.getId())
                        .orElseThrow(() -> new MainServiceException(
                                "Закрепление пользователя за автомобилем не найдено"
                        ));

        Long resultTruckId =
                assignment.getTruckId() != null
                        ? assignment.getTruckId()
                        : dbAssignment.getTruckId();

        Long resultUserId =
                assignment.getUserId() != null
                        ? assignment.getUserId()
                        : dbAssignment.getUserId();

        TruckUserAssignmentType resultType =
                assignment.getAssignmentType() != null
                        ? assignment.getAssignmentType()
                        : dbAssignment.getAssignmentType();

        TruckUserAssignmentStatus resultStatus =
                assignment.getStatus() != null
                        ? assignment.getStatus()
                        : dbAssignment.getStatus();

        LocalDateTime resultDateFrom =
                assignment.getDateFrom() != null
                        ? assignment.getDateFrom()
                        : dbAssignment.getDateFrom();

        LocalDateTime resultDateTo =
                assignment.getDateTo() != null
                        ? assignment.getDateTo()
                        : dbAssignment.getDateTo();


//        if(assignment.getIsPrimaryDriver() != null){
//            validatePrimaryDriver(assignment);
//        }

        validateTruckId(resultTruckId);
        validateUserId(resultUserId);
        validateDates(resultDateFrom, resultDateTo);

        if (assignment.getCreatedByUserId() != null) {
            validateUserId(assignment.getCreatedByUserId());
        }

//        if (resultStatus == TruckUserAssignmentStatus.ACTIVE
//                && assignmentRepository
//                .existsByTruckIdAndUserIdAndAssignmentTypeAndStatusAndIdNot(
//                        resultTruckId,
//                        resultUserId,
//                        resultType,
//                        TruckUserAssignmentStatus.ACTIVE,
//                        assignment.getId()
//                )) {
//
//            throw new MainServiceException(
//                    "Такое активное закрепление уже существует"
//            );
//        }

        /*
         * Если запись становится активной фактической,
         * закрываем другую активную ACTUAL-запись автомобиля.
         */
        if (resultType == TruckUserAssignmentType.ACTUAL
                && resultStatus == TruckUserAssignmentStatus.ACTIVE) {

            assignmentRepository
                    .findFirstByTruckIdAndAssignmentTypeAndStatusOrderByDateFromDesc(
                            resultTruckId,
                            TruckUserAssignmentType.ACTUAL,
                            TruckUserAssignmentStatus.ACTIVE
                    )
                    .filter(current ->
                            !current.getId().equals(dbAssignment.getId()))
                    .ifPresent(current -> {
                        current.setStatus(
                                TruckUserAssignmentStatus.COMPLETED
                        );

                        current.setDateTo(LocalDateTime.now());

                        assignmentRepository.save(current);
                    });
        }

        dbAssignment.setTruckId(resultTruckId);
        dbAssignment.setUserId(resultUserId);
        dbAssignment.setAssignmentType(resultType);
        dbAssignment.setStatus(resultStatus);
        dbAssignment.setDateFrom(resultDateFrom);

//        if(assignment.getIsPrimaryDriver() != null){
//            dbAssignment.setIsPrimaryDriver(assignment.getIsPrimaryDriver());
//        }

        if (assignment.getDateTo() != null) {
            dbAssignment.setDateTo(assignment.getDateTo());
        }

        if (assignment.getComment() != null) {
            dbAssignment.setComment(assignment.getComment());
        }

        if (assignment.getCreatedByUserId() != null) {
            dbAssignment.setCreatedByUserId(
                    assignment.getCreatedByUserId()
            );
        }

        /*
         * При штатном завершении, истечении или отмене
         * автоматически ставим дату окончания,
         * если она не передана.
         */
        if (resultStatus != TruckUserAssignmentStatus.ACTIVE
                && dbAssignment.getDateTo() == null) {

            dbAssignment.setDateTo(LocalDateTime.now());
        }

        /*
         * updatedAt автоматически заполнится через @PreUpdate.
         */

        return assignmentRepository.save(dbAssignment);
    }

    /**
     * Штатное завершение закрепления.
     * Завершает с текущей датой и временем
     */
    @Audited(operation = "COMPLETE_TRUCK_USER_ASSIGNMENT")
    @Transactional
    public TruckUserAssignment complete(Long id) {

        TruckUserAssignment assignment = getById(id);

        if (assignment.getStatus()
                != TruckUserAssignmentStatus.ACTIVE) {

            throw new MainServiceException(
                    "Закрепление уже не является активным"
            );
        }

        assignment.setStatus(
                TruckUserAssignmentStatus.COMPLETED
        );

        assignment.setDateTo(LocalDateTime.now());

        return assignmentRepository.save(assignment);
    }
    
    /**
     * Штатное завершение закрепления.
     * @param id
     * @param dateTime дата и время завершения
     * @return
     */
    @Audited(operation = "COMPLETE_TRUCK_USER_ASSIGNMENT")
    @Transactional
    public TruckUserAssignment complete(Long id, LocalDateTime dateTime) {
    	
    	TruckUserAssignment assignment = getById(id);
    	
    	if (assignment.getStatus()
    			!= TruckUserAssignmentStatus.ACTIVE) {
    		
    		throw new MainServiceException(
    				"Закрепление уже не является активным"
    				);
    	}
    	
    	assignment.setStatus(
    			TruckUserAssignmentStatus.COMPLETED
    			);
    	
    	assignment.setDateTo(dateTime);
    	
    	return assignmentRepository.save(assignment);
    }
    
    

    /**
     * Отмена закрепления.
     */
    @Audited(operation = "CANCEL_TRUCK_USER_ASSIGNMENT")
    @Transactional
    public TruckUserAssignment cancel(Long id) {

        System.out.println("=== CANCEL ASSIGNMENT START ===");
        System.out.println("Assignment ID: " + id);

        TruckUserAssignment assignment = getById(id);

        System.out.println("Assignment найден: " + (assignment != null));

        if (assignment == null) {
            System.out.println("Assignment == null");
            throw new MainServiceException("Закрепление не найдено");
        }

        System.out.println("Assignment ID: " + assignment.getId());
        System.out.println("Truck ID: " + assignment.getTruckId());
        System.out.println("User ID: " + assignment.getUserId());
        System.out.println("Status ДО отмены: " + assignment.getStatus());
        System.out.println("DateFrom: " + assignment.getDateFrom());
        System.out.println("DateTo ДО отмены: " + assignment.getDateTo());

        if (assignment.getStatus()
                != TruckUserAssignmentStatus.ACTIVE) {

            System.out.println(
                    "ОТМЕНА НЕВОЗМОЖНА. Текущий статус: "
                            + assignment.getStatus()
            );

            throw new MainServiceException(
                    "Можно отменить только активное закрепление"
            );
        }

        // =========================
        // Отмена Assignment
        // =========================

        assignment.setStatus(
                TruckUserAssignmentStatus.CANCELLED
        );

        assignment.setDateTo(LocalDateTime.now());

        System.out.println("Assignment новый статус: "
                + assignment.getStatus());

        System.out.println("Assignment новый DateTo: "
                + assignment.getDateTo());


        // =========================
        // RouteSheet
        // =========================

        System.out.println("=== ПРОВЕРКА ROUTE SHEET ===");

        System.out.println("Ищем RouteSheet по truckId: "
                + assignment.getTruckId());

        boolean isExistSheet =
                routeSheetRepository
                        .existsByIdTruckUserAssignmentAndStatus(
                                assignment.getId(),
                                RouteSheetStatus.ACTIVE
                        );

        System.out.println("RouteSheet ACTIVE существует: "
                + isExistSheet);

        if (isExistSheet) {

            System.out.println(
                    "Ищем ACTIVE RouteSheet по assignmentId: "
                            + assignment.getId()
            );

            RouteSheet routeSheet =
                    routeSheetRepository
                            .findByIdTruckUserAssignmentAndStatus(
                                    assignment.getId(),
                                    RouteSheetStatus.ACTIVE
                            );

            System.out.println("RouteSheet найден: "
                    + (routeSheet != null));

            if (routeSheet != null) {

                System.out.println("RouteSheet ID: "
                        + routeSheet.getIdRouteSheet());

                System.out.println("RouteSheet status ДО: "
                        + routeSheet.getStatus());

                routeSheet.setStatus(
                        RouteSheetStatus.CANCELLED
                );

                routeSheet.setUpdatedAt(
                        LocalDateTime.now()
                );

                System.out.println("RouteSheet status ПОСЛЕ: "
                        + routeSheet.getStatus());

                System.out.println("RouteSheet updatedAt: "
                        + routeSheet.getUpdatedAt());

                RouteSheet savedRouteSheet =
                        routeSheetRepository.save(routeSheet);

                System.out.println("RouteSheet СОХРАНЁН");
                System.out.println("RouteSheet ID: "
                        + savedRouteSheet.getIdRouteSheet());
                System.out.println("RouteSheet итоговый status: "
                        + savedRouteSheet.getStatus());

            } else {
                System.out.println(
                        "ВНИМАНИЕ: existsBy... вернул TRUE, "
                                + "но findBy... вернул NULL"
                );
            }

        } else {
            System.out.println(
                    "ACTIVE RouteSheet не найден — статус RouteSheet не меняем"
            );
        }


        // =========================
        // OrderTruck
        // =========================

        System.out.println("=== ПРОВЕРКА ORDER TRUCK ===");

        boolean isExistTruckOrder =
                orderTruckRepository
                        .existsByIdTruckUserAssigmentAndStatus(
                                assignment.getId(),
                                OrderTruckAssigmentStatus.ACTIVE
                        );

        System.out.println("OrderTruck ACTIVE существует: "
                + isExistTruckOrder);

        if (isExistTruckOrder) {

            OrderTruck orderTruck =
                    orderTruckRepository
                            .findByIdTruckUserAssigmentAndStatus(
                                    assignment.getId(),
                                    OrderTruckAssigmentStatus.ACTIVE
                            );

            System.out.println("OrderTruck найден: "
                    + (orderTruck != null));

            if (orderTruck != null) {

                System.out.println("OrderTruck ID: "
                        + orderTruck.getId());

                System.out.println("OrderTruck status ДО: "
                        + orderTruck.getStatus());

                orderTruck.setStatus(
                        OrderTruckAssigmentStatus.CANCELLED
                );

                System.out.println("OrderTruck status ПОСЛЕ: "
                        + orderTruck.getStatus());

                OrderTruck savedOrderTruck =
                        orderTruckRepository.save(orderTruck);

                System.out.println("OrderTruck СОХРАНЁН");
                System.out.println("OrderTruck ID: "
                        + savedOrderTruck.getId());
                System.out.println("OrderTruck итоговый status: "
                        + savedOrderTruck.getStatus());

            } else {
                System.out.println(
                        "ВНИМАНИЕ: existsBy... TRUE, "
                                + "но findBy... вернул NULL"
                );
            }

        } else {
            System.out.println(
                    "ACTIVE OrderTruck не найден"
            );
        }


        // =========================
        // Сохранение Assignment
        // =========================

        TruckUserAssignment savedAssignment =
                assignmentRepository.save(assignment);

        System.out.println("=== ASSIGNMENT СОХРАНЁН ===");
        System.out.println("Assignment ID: "
                + savedAssignment.getId());

        System.out.println("Assignment status: "
                + savedAssignment.getStatus());

        System.out.println("Assignment DateTo: "
                + savedAssignment.getDateTo());

        System.out.println("=== CANCEL ASSIGNMENT END ===");

        return savedAssignment;
    }

    private void completeCurrentActualAssignment(Long truckId) {

        assignmentRepository
                .findFirstByTruckIdAndAssignmentTypeAndStatusOrderByDateFromDesc(
                        truckId,
                        TruckUserAssignmentType.ACTUAL,
                        TruckUserAssignmentStatus.ACTIVE
                )
                .ifPresent(current -> {
                    current.setStatus(
                            TruckUserAssignmentStatus.COMPLETED
                    );

                    current.setDateTo(LocalDateTime.now());

                    assignmentRepository.save(current);
                });
    }

    private void validateRequiredFields(
            TruckUserAssignment assignment) {

        if (assignment == null) {
            throw new MainServiceException(
                    "Не переданы данные закрепления"
            );
        }

        if (assignment.getTruckId() == null) {
            throw new MainServiceException(
                    "Не указан TruckId"
            );
        }

        if (assignment.getUserId() == null) {
            throw new MainServiceException(
                    "Не указан UserId"
            );
        }

        if (assignment.getAssignmentType() == null) {
            throw new MainServiceException(
                    "Не указан тип закрепления"
            );
        }
    }

    private void validateReferences(
            TruckUserAssignment assignment) {

        validateTruckId(assignment.getTruckId());
        validateUserId(assignment.getUserId());

        if (assignment.getCreatedByUserId() != null) {
            validateUserId(
                    assignment.getCreatedByUserId()
            );
        }
    }

    private void validateTruckId(Long truckId) {

        if (truckId == null) {
            throw new MainServiceException(
                    "Не указан TruckId"
            );
        }

        if (!truckRepository.existsById(truckId)) {
            throw new MainServiceException(
                    "Транспортное средство не найдено"
            );
        }
    }

    private void validateUserId(Long userId) {

        if (userId == null) {
            throw new MainServiceException(
                    "Не указан UserId"
            );
        }

        if (!userRepository.existsById(userId)) {
            throw new MainServiceException(
                    "Пользователь не найден"
            );
        }
    }

    private void validateDates(
            LocalDateTime dateFrom,
            LocalDateTime dateTo) {

        if (dateFrom != null
                && dateTo != null
                && dateTo.isBefore(dateFrom)) {

            throw new MainServiceException(
                    "Дата окончания закрепления не может быть раньше даты начала"
            );
        }
    }

}
