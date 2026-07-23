package dccargo.dcargoservice.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dccargo.dcargoservice.dto.dcargo.AssignTruckDriversDTO;
import dccargo.dcargoservice.dto.dcargo.DriverWorkDayDTO;
import dccargo.dcargoservice.model.dcargo.DriverScheduleException;
import dccargo.dcargoservice.model.dcargo.DriverWorkSchedule;
import dccargo.dcargoservice.model.dcargo.WorkSchedule;
import dccargo.dcargoservice.model.dcargo.WorkScheduleService;
import dccargo.dcargoservice.service.dcargo.DriverScheduleExceptionService;
import dccargo.dcargoservice.service.dcargo.DriverWorkScheduleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/work-schedule")
@RequiredArgsConstructor
public class WorkScheduleController {
	
	private final WorkScheduleService workScheduleService;
    private final DriverWorkScheduleService driverWorkScheduleService;
    private final DriverScheduleExceptionService exceptionService;
    
    /*
     * ============================================================
     * ШАБЛОНЫ РАБОЧИХ ГРАФИКОВ
     * ============================================================
     */

    /**
     * Создать новый шаблон графика.
     */
    @PostMapping("/create")
    public ResponseEntity<WorkSchedule> createWorkSchedule(
            @RequestBody WorkSchedule workSchedule
    ) {
        return ResponseEntity.ok(
                workScheduleService.create(workSchedule)
        );
    }

    /**
     * Обновить шаблон графика.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkSchedule> updateWorkSchedule(
            @PathVariable Long id,
            @RequestBody WorkSchedule workSchedule
    ) {
        return ResponseEntity.ok(
                workScheduleService.update(id, workSchedule)
        );
    }

    /**
     * Получить шаблон графика по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkSchedule> getWorkScheduleById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                workScheduleService.getById(id)
        );
    }

    /**
     * Получить все шаблоны графиков.
     */
    @GetMapping("/all")
    public ResponseEntity<List<WorkSchedule>> getAllWorkSchedules() {
        return ResponseEntity.ok(
                workScheduleService.getAll()
        );
    }

    /**
     * Удалить шаблон графика.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkSchedule(
            @PathVariable Long id
    ) {
        workScheduleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /*
     * ============================================================
     * НАЗНАЧЕНИЯ ГРАФИКОВ ВОДИТЕЛЯМ
     * ============================================================
     */

    /**
     * Создать индивидуальное назначение графика.
     */
    @PostMapping("/assignment/create")
    public ResponseEntity<DriverWorkSchedule> createAssignment(
            @RequestBody DriverWorkSchedule assignment
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService.create(assignment)
        );
    }

    /**
     * Закрепить сразу двух водителей за автомобилем.
     */
    @PostMapping("/assignment/assign-two-drivers")
    public ResponseEntity<List<DriverWorkSchedule>> assignTwoDrivers(
            @RequestBody AssignTruckDriversDTO request
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService.assignTwoDrivers(request)
        );
    }

    /**
     * Обновить назначение графика.
     */
    @PutMapping("/assignment/{id}")
    public ResponseEntity<DriverWorkSchedule> updateAssignment(
            @PathVariable Long id,
            @RequestBody DriverWorkSchedule assignment
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService.update(id, assignment)
        );
    }

    /**
     * Закрыть назначение графика.
     */
    @PutMapping("/assignment/{id}/close")
    public ResponseEntity<DriverWorkSchedule> closeAssignment(
            @PathVariable Long id,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateTo
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService.close(id, dateTo)
        );
    }

    /**
     * Получить назначение по ID.
     */
    @GetMapping("/assignment/{id}")
    public ResponseEntity<DriverWorkSchedule> getAssignmentById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService.getById(id)
        );
    }

    /**
     * Получить все назначения автомобиля.
     */
    @GetMapping("/assignment/truck/{truckId}")
    public ResponseEntity<List<DriverWorkSchedule>>
    getAssignmentsByTruckId(
            @PathVariable Long truckId
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService.getByTruckId(truckId)
        );
    }

    /**
     * Получить только активные назначения автомобиля.
     */
    @GetMapping("/assignment/truck/{truckId}/active")
    public ResponseEntity<List<DriverWorkSchedule>>
    getActiveAssignmentsByTruckId(
            @PathVariable Long truckId
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService
                        .getActiveByTruckId(truckId)
        );
    }

    /**
     * Получить все назначения водителя.
     */
    @GetMapping("/assignment/user/{userId}")
    public ResponseEntity<List<DriverWorkSchedule>>
    getAssignmentsByUserId(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService.getByUserId(userId)
        );
    }

    /*
     * ============================================================
     * КАЛЕНДАРЬ
     * ============================================================
     */

    /**
     * Получить календарь автомобиля за период.
     *
     * Пример:
     * /api/work-schedule/calendar/truck/5
     * ?dateFrom=2026-07-22
     * &dateTo=2026-07-31
     */
    @GetMapping("/calendar/truck/{truckId}")
    public ResponseEntity<List<DriverWorkDayDTO>>
    getTruckCalendar(
            @PathVariable Long truckId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFrom,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateTo
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService.getTruckCalendar(
                        truckId,
                        dateFrom,
                        dateTo
                )
        );
    }

    /**
     * Проверить, работает ли водитель в конкретную дату.
     */
    @GetMapping("/calendar/user/{userId}/working")
    public ResponseEntity<Boolean> isDriverWorking(
            @PathVariable Long userId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(
                driverWorkScheduleService.isDriverWorking(
                        userId,
                        date
                )
        );
    }

    /*
     * ============================================================
     * ИСКЛЮЧЕНИЯ ГРАФИКА
     * ============================================================
     */

    /**
     * Создать исключение графика.
     */
    @PostMapping("/exception/create")
    public ResponseEntity<DriverScheduleException> createException(
            @RequestBody DriverScheduleException exception
    ) {
        return ResponseEntity.ok(
                exceptionService.create(exception)
        );
    }

    /**
     * Обновить исключение графика.
     */
    @PutMapping("/exception/{id}")
    public ResponseEntity<DriverScheduleException> updateException(
            @PathVariable Long id,
            @RequestBody DriverScheduleException exception
    ) {
        return ResponseEntity.ok(
                exceptionService.update(id, exception)
        );
    }

    /**
     * Получить исключение по ID.
     */
    @GetMapping("/exception/{id}")
    public ResponseEntity<DriverScheduleException> getExceptionById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                exceptionService.getById(id)
        );
    }

    /**
     * Получить исключение водителя на конкретную дату.
     */
    @GetMapping("/exception/user/{userId}/date")
    public ResponseEntity<DriverScheduleException>
    getExceptionByUserAndDate(
            @PathVariable Long userId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(
                exceptionService.getByUserAndDate(
                        userId,
                        date
                )
        );
    }

    /**
     * Получить исключения автомобиля за период.
     */
    @GetMapping("/exception/truck/{truckId}")
    public ResponseEntity<List<DriverScheduleException>>
    getExceptionsByTruckAndPeriod(
            @PathVariable Long truckId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFrom,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateTo
    ) {
        return ResponseEntity.ok(
                exceptionService.getByTruckAndPeriod(
                        truckId,
                        dateFrom,
                        dateTo
                )
        );
    }

    /**
     * Получить исключения водителя за период.
     */
    @GetMapping("/exception/user/{userId}")
    public ResponseEntity<List<DriverScheduleException>>
    getExceptionsByUserAndPeriod(
            @PathVariable Long userId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFrom,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateTo
    ) {
        return ResponseEntity.ok(
                exceptionService.getByUserAndPeriod(
                        userId,
                        dateFrom,
                        dateTo
                )
        );
    }

    /**
     * Удалить исключение графика.
     */
    @DeleteMapping("/exception/{id}")
    public ResponseEntity<Void> deleteException(
            @PathVariable Long id
    ) {
        exceptionService.delete(id);

        return ResponseEntity.noContent().build();
    }


}
