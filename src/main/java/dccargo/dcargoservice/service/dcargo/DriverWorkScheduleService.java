package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dccargo.dcargoservice.dto.dcargo.AssignTruckDriversDTO;
import dccargo.dcargoservice.dto.dcargo.DriverWorkDayDTO;
import dccargo.dcargoservice.enums.DriverScheduleExceptionType;
import dccargo.dcargoservice.model.dcargo.DriverScheduleException;
import dccargo.dcargoservice.model.dcargo.DriverWorkSchedule;
import dccargo.dcargoservice.model.dcargo.WorkSchedule;
import dccargo.dcargoservice.model.dcargo.WorkScheduleService;
import dccargo.dcargoservice.repository.dcargo.DriverScheduleExceptionRepository;
import dccargo.dcargoservice.repository.dcargo.DriverWorkScheduleRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverWorkScheduleService {
	
	private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_INACTIVE = "INACTIVE";

    private final DriverWorkScheduleRepository driverWorkScheduleRepository;
    private final DriverScheduleExceptionRepository exceptionRepository;
    private final WorkScheduleService workScheduleService;

    /**
     * Создание индивидуального назначения графика.
     */
    public DriverWorkSchedule create(
            DriverWorkSchedule assignment
    ) {
        validateAssignment(assignment);

        WorkSchedule schedule =
                workScheduleService.getById(
                        assignment.getScheduleId()
                );

        validateCycleOffset(
                assignment.getCycleOffset(),
                schedule
        );

        LocalDate dateTo = assignment.getDateTo() != null
                ? assignment.getDateTo()
                : LocalDate.of(9999, 12, 31);

        long intersections =
                driverWorkScheduleRepository
                        .countUserScheduleIntersections(
                                assignment.getUserId(),
                                assignment.getDateFrom(),
                                dateTo,
                                null
                        );

        if (intersections > 0) {
            throw new MainServiceException(
                    "У водителя с ID "
                            + assignment.getUserId()
                            + " уже существует график, пересекающийся "
                            + "с указанным периодом"
            );
        }

        assignment.setId(null);

        if (assignment.getStatus() == null) {
            assignment.setStatus(STATUS_ACTIVE);
        }

        DriverWorkSchedule saved =
                driverWorkScheduleRepository.save(assignment);

        log.info(
                "Водителю {} назначен график {} для автомобиля {}",
                saved.getUserId(),
                saved.getScheduleId(),
                saved.getTruckId()
        );

        return saved;
    }

    /**
     * Закрепляет сразу двух водителей за автомобилем
     * с противоположными фазами графика.
     */
    @Transactional
    public List<DriverWorkSchedule> assignTwoDrivers(
            AssignTruckDriversDTO request
    ) {
        validateTwoDriversRequest(request);

        WorkSchedule schedule =
                workScheduleService.getById(
                        request.getScheduleId()
                );

        LocalDate dateFrom = request.getDateFrom() != null
                ? request.getDateFrom()
                : request.getCycleStartDate();

        /*
         * Закрываем предыдущие активные назначения автомобиля
         * днём перед началом нового графика.
         */
        LocalDate previousDate = dateFrom.minusDays(1);

        int closed =
                driverWorkScheduleRepository
                        .closeActiveSchedulesByTruckId(
                                request.getTruckId(),
                                previousDate
                        );

        if (closed > 0) {
            log.info(
                    "Закрыто {} предыдущих назначений автомобиля {}",
                    closed,
                    request.getTruckId()
            );
        }

        DriverWorkSchedule firstDriver =
                DriverWorkSchedule.builder()
                        .truckId(request.getTruckId())
                        .userId(request.getFirstDriverId())
                        .scheduleId(request.getScheduleId())
                        .cycleStartDate(
                                request.getCycleStartDate()
                        )
                        .cycleOffset(0)
                        .dateFrom(dateFrom)
                        .status(STATUS_ACTIVE)
                        .build();

        /*
         * Второй водитель начинает цикл
         * после рабочих дней первого.
         *
         * Для графика 2/2 offset будет равен 2.
         */
        DriverWorkSchedule secondDriver =
                DriverWorkSchedule.builder()
                        .truckId(request.getTruckId())
                        .userId(request.getSecondDriverId())
                        .scheduleId(request.getScheduleId())
                        .cycleStartDate(
                                request.getCycleStartDate()
                        )
                        .cycleOffset(schedule.getWorkDays())
                        .dateFrom(dateFrom)
                        .status(STATUS_ACTIVE)
                        .build();

        validateNoUserIntersection(firstDriver);
        validateNoUserIntersection(secondDriver);

        List<DriverWorkSchedule> saved =
                driverWorkScheduleRepository.saveAll(
                        List.of(firstDriver, secondDriver)
                );

        log.info(
                "За автомобилем {} закреплены водители {} и {}",
                request.getTruckId(),
                request.getFirstDriverId(),
                request.getSecondDriverId()
        );

        return saved;
    }

    @Transactional
    public DriverWorkSchedule update(
            Long id,
            DriverWorkSchedule request
    ) {
        DriverWorkSchedule existing = getById(id);

        if (request.getTruckId() != null) {
            existing.setTruckId(request.getTruckId());
        }

        if (request.getUserId() != null) {
            existing.setUserId(request.getUserId());
        }

        if (request.getScheduleId() != null) {
            workScheduleService.getById(request.getScheduleId());
            existing.setScheduleId(request.getScheduleId());
        }

        if (request.getCycleStartDate() != null) {
            existing.setCycleStartDate(
                    request.getCycleStartDate()
            );
        }

        if (request.getCycleOffset() != null) {
            existing.setCycleOffset(
                    request.getCycleOffset()
            );
        }

        if (request.getDateFrom() != null) {
            existing.setDateFrom(request.getDateFrom());
        }

        if (request.getDateTo() != null) {
            existing.setDateTo(request.getDateTo());
        }

        if (request.getStatus() != null) {
            validateStatus(request.getStatus());
            existing.setStatus(request.getStatus());
        }

        validateAssignment(existing);

        WorkSchedule schedule =
                workScheduleService.getById(
                        existing.getScheduleId()
                );

        validateCycleOffset(
                existing.getCycleOffset(),
                schedule
        );

        validateNoUserIntersection(existing);

        log.info(
                "Обновлено назначение рабочего графика id={}",
                id
        );

        return existing;
    }

    @Transactional
    public DriverWorkSchedule close(
            Long id,
            LocalDate dateTo
    ) {
        DriverWorkSchedule assignment = getById(id);

        if (dateTo == null) {
            throw new MainServiceException(
                    "Не указана дата окончания графика"
            );
        }

        if (dateTo.isBefore(assignment.getDateFrom())) {
            throw new MainServiceException(
                    "Дата окончания не может быть раньше даты начала"
            );
        }

        assignment.setDateTo(dateTo);
        assignment.setStatus(STATUS_INACTIVE);

        log.info(
                "Закрыто назначение графика id={}, dateTo={}",
                id,
                dateTo
        );

        return assignment;
    }

    public DriverWorkSchedule getById(Long id) {
        if (id == null) {
            throw new MainServiceException(
                    "Не указан ID назначения графика"
            );
        }

        return driverWorkScheduleRepository.findById(id)
                .orElseThrow(() -> new MainServiceException(
                        "Назначение рабочего графика с ID "
                                + id
                                + " не найдено"
                ));
    }

    public List<DriverWorkSchedule> getByTruckId(
            Long truckId
    ) {
        validateId(truckId, "ID автомобиля");

        return driverWorkScheduleRepository
                .findAllByTruckIdOrderByDateFromDesc(truckId);
    }

    public List<DriverWorkSchedule> getActiveByTruckId(
            Long truckId
    ) {
        validateId(truckId, "ID автомобиля");

        return driverWorkScheduleRepository
                .findAllByTruckIdAndStatusOrderByDateFromDesc(
                        truckId,
                        STATUS_ACTIVE
                );
    }

    public List<DriverWorkSchedule> getByUserId(
            Long userId
    ) {
        validateId(userId, "ID водителя");

        return driverWorkScheduleRepository
                .findAllByUserIdOrderByDateFromDesc(userId);
    }

    /**
     * Возвращает календарь автомобиля.
     *
     * На каждый день создаётся результат
     * для каждого назначенного водителя.
     */
    public List<DriverWorkDayDTO> getTruckCalendar(
            Long truckId,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        validatePeriod(truckId, dateFrom, dateTo);

        List<DriverWorkSchedule> assignments =
                driverWorkScheduleRepository
                        .findAllForTruckAndPeriod(
                                truckId,
                                dateFrom,
                                dateTo
                        );

        if (assignments.isEmpty()) {
            return List.of();
        }

        List<DriverScheduleException> exceptions =
                exceptionRepository
                        .findAllByTruckIdAndExceptionDateBetweenOrderByExceptionDateAsc(
                                truckId,
                                dateFrom,
                                dateTo
                        );

        Map<String, DriverScheduleException> exceptionMap =
                exceptions.stream()
                        .collect(Collectors.toMap(
                                exception -> buildExceptionKey(
                                        exception.getUserId(),
                                        exception.getExceptionDate()
                                ),
                                Function.identity(),
                                (first, second) -> second
                        ));

        Map<Long, WorkSchedule> scheduleMap =
                assignments.stream()
                        .map(DriverWorkSchedule::getScheduleId)
                        .distinct()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                workScheduleService::getById
                        ));

        List<DriverWorkDayDTO> result = new ArrayList<>();

        LocalDate currentDate = dateFrom;

        while (!currentDate.isAfter(dateTo)) {
            for (DriverWorkSchedule assignment : assignments) {

                if (!isAssignmentActiveOnDate(
                        assignment,
                        currentDate
                )) {
                    continue;
                }

                WorkSchedule schedule =
                        scheduleMap.get(
                                assignment.getScheduleId()
                        );

                boolean scheduledWorking =
                        isWorkingDay(
                                currentDate,
                                assignment,
                                schedule
                        );

                DriverScheduleException exception =
                        exceptionMap.get(
                                buildExceptionKey(
                                        assignment.getUserId(),
                                        currentDate
                                )
                        );

                result.add(
                        buildDayResult(
                                currentDate,
                                assignment,
                                scheduledWorking,
                                exception
                        )
                );
            }

            currentDate = currentDate.plusDays(1);
        }

        return result.stream()
                .sorted(
                        Comparator
                                .comparing(
                                        DriverWorkDayDTO::getDate
                                )
                                .thenComparing(
                                        DriverWorkDayDTO::getUserId
                                )
                )
                .toList();
    }

    public boolean isDriverWorking(
            Long userId,
            LocalDate date
    ) {
        validateId(userId, "ID водителя");

        if (date == null) {
            throw new MainServiceException(
                    "Не указана дата"
            );
        }

        List<DriverWorkSchedule> assignments =
                driverWorkScheduleRepository
                        .findActiveByUserIdAndDate(
                                userId,
                                date
                        );

        if (assignments.isEmpty()) {
            return false;
        }

        DriverScheduleException exception =
                exceptionRepository
                        .findByUserIdAndExceptionDate(
                                userId,
                                date
                        )
                        .orElse(null);

        if (exception != null) {
            return switch (exception.getExceptionType()) {
                case WORKING, REPLACEMENT -> true;
                case DAY_OFF, VACATION, SICK_LEAVE -> false;
            };
        }

        DriverWorkSchedule assignment = assignments.get(0);

        WorkSchedule schedule =
                workScheduleService.getById(
                        assignment.getScheduleId()
                );

        return isWorkingDay(
                date,
                assignment,
                schedule
        );
    }

    private DriverWorkDayDTO buildDayResult(
            LocalDate date,
            DriverWorkSchedule assignment,
            boolean scheduledWorking,
            DriverScheduleException exception
    ) {
        boolean actualWorking = scheduledWorking;
        String status = scheduledWorking
                ? DriverScheduleExceptionType.WORKING.name()
                : DriverScheduleExceptionType.DAY_OFF.name();

        String statusDescription = scheduledWorking
                ? DriverScheduleExceptionType.WORKING.getDescription()
                : DriverScheduleExceptionType.DAY_OFF.getDescription();

        Long replacementUserId = null;
        boolean exceptionApplied = false;
        String comment = null;

        if (exception != null) {
            exceptionApplied = true;
            comment = exception.getComment();

            DriverScheduleExceptionType type =
                    exception.getExceptionType();

            status = type.name();
            statusDescription = type.getDescription();

            switch (type) {
                case WORKING -> actualWorking = true;

                case DAY_OFF, VACATION, SICK_LEAVE ->
                        actualWorking = false;

                case REPLACEMENT -> {
                    /*
                     * Основной водитель фактически не работает.
                     * Вместо него выходит replacementUserId.
                     */
                    actualWorking = false;
                    replacementUserId =
                            exception.getReplacementUserId();
                }
            }
        }

        return DriverWorkDayDTO.builder()
                .date(date)
                .truckId(assignment.getTruckId())
                .userId(assignment.getUserId())
                .scheduledWorking(scheduledWorking)
                .actualWorking(actualWorking)
                .status(status)
                .statusDescription(statusDescription)
                .replacementUserId(replacementUserId)
                .exceptionApplied(exceptionApplied)
                .comment(comment)
                .build();
    }

    private boolean isWorkingDay(
            LocalDate date,
            DriverWorkSchedule assignment,
            WorkSchedule schedule
    ) {
        int cycleLength =
                schedule.getWorkDays()
                        + schedule.getRestDays();

        long daysFromStart =
                ChronoUnit.DAYS.between(
                        assignment.getCycleStartDate(),
                        date
                );

        int cycleDay = Math.floorMod(
                Math.toIntExact(daysFromStart)
                        + assignment.getCycleOffset(),
                cycleLength
        );

        return cycleDay < schedule.getWorkDays();
    }

    private boolean isAssignmentActiveOnDate(
            DriverWorkSchedule assignment,
            LocalDate date
    ) {
        boolean afterStart =
                !date.isBefore(assignment.getDateFrom());

        boolean beforeEnd =
                assignment.getDateTo() == null
                        || !date.isAfter(
                                assignment.getDateTo()
                        );

        return afterStart && beforeEnd;
    }

    private void validateAssignment(
            DriverWorkSchedule assignment
    ) {
        if (assignment == null) {
            throw new MainServiceException(
                    "Не переданы данные назначения графика"
            );
        }

        validateId(assignment.getUserId(), "ID водителя");
        validateId(assignment.getScheduleId(), "ID графика");

        if (assignment.getCycleStartDate() == null) {
            throw new MainServiceException(
                    "Не указана дата начала цикла"
            );
        }

        if (assignment.getDateFrom() == null) {
            throw new MainServiceException(
                    "Не указана дата начала действия графика"
            );
        }

        if (assignment.getDateTo() != null
                && assignment.getDateTo()
                .isBefore(assignment.getDateFrom())) {

            throw new MainServiceException(
                    "Дата окончания графика не может быть раньше даты начала"
            );
        }

        if (assignment.getStatus() != null) {
            validateStatus(assignment.getStatus());
        }
    }

    private void validateTwoDriversRequest(
            AssignTruckDriversDTO request
    ) {
        if (request == null) {
            throw new MainServiceException(
                    "Не переданы данные для закрепления водителей"
            );
        }

        validateId(request.getTruckId(), "ID автомобиля");
        validateId(request.getScheduleId(), "ID графика");
        validateId(
                request.getFirstDriverId(),
                "ID первого водителя"
        );
        validateId(
                request.getSecondDriverId(),
                "ID второго водителя"
        );

        if (request.getFirstDriverId()
                .equals(request.getSecondDriverId())) {

            throw new MainServiceException(
                    "Нельзя закрепить одного водителя дважды"
            );
        }

        if (request.getCycleStartDate() == null) {
            throw new MainServiceException(
                    "Не указана дата начала цикла"
            );
        }

        if (request.getDateFrom() != null
                && request.getDateFrom()
                .isBefore(request.getCycleStartDate())) {

            throw new MainServiceException(
                    "Дата начала назначения не может быть раньше начала цикла"
            );
        }
    }

    private void validateNoUserIntersection(
            DriverWorkSchedule assignment
    ) {
        LocalDate dateTo = assignment.getDateTo() != null
                ? assignment.getDateTo()
                : LocalDate.of(9999, 12, 31);

        long count =
                driverWorkScheduleRepository
                        .countUserScheduleIntersections(
                                assignment.getUserId(),
                                assignment.getDateFrom(),
                                dateTo,
                                assignment.getId()
                        );

        if (count > 0) {
            throw new MainServiceException(
                    "У водителя с ID "
                            + assignment.getUserId()
                            + " уже есть график на указанный период"
            );
        }
    }

    private void validateCycleOffset(
            Integer cycleOffset,
            WorkSchedule schedule
    ) {
        if (cycleOffset == null) {
            throw new MainServiceException(
                    "Не указано смещение рабочего цикла"
            );
        }

        int cycleLength =
                schedule.getWorkDays()
                        + schedule.getRestDays();

        if (cycleOffset < 0
                || cycleOffset >= cycleLength) {

            throw new MainServiceException(
                    "Смещение цикла должно находиться в диапазоне от 0 до "
                            + (cycleLength - 1)
            );
        }
    }

    private void validateStatus(String status) {
        if (!STATUS_ACTIVE.equals(status)
                && !STATUS_INACTIVE.equals(status)) {

            throw new MainServiceException(
                    "Допустимые статусы назначения: ACTIVE, INACTIVE"
            );
        }
    }

    private void validatePeriod(
            Long truckId,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        validateId(truckId, "ID автомобиля");

        if (dateFrom == null || dateTo == null) {
            throw new MainServiceException(
                    "Не указан период календаря"
            );
        }

        if (dateTo.isBefore(dateFrom)) {
            throw new MainServiceException(
                    "Конечная дата не может быть раньше начальной"
            );
        }

        if (ChronoUnit.DAYS.between(dateFrom, dateTo) > 366) {
            throw new MainServiceException(
                    "За один запрос нельзя получить календарь более чем за 366 дней"
            );
        }
    }

    private void validateId(
            Long id,
            String fieldName
    ) {
        if (id == null || id <= 0) {
            throw new MainServiceException(
                    "Некорректное значение поля: " + fieldName
            );
        }
    }

    private String buildExceptionKey(
            Long userId,
            LocalDate date
    ) {
        return userId + "_" + date;
    }

}
