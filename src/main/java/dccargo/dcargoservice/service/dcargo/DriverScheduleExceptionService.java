package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import dccargo.dcargoservice.enums.DriverScheduleExceptionType;
import dccargo.dcargoservice.model.dcargo.DriverScheduleException;
import dccargo.dcargoservice.repository.dcargo.DriverScheduleExceptionRepository;
import dccargo.dcargoservice.repository.dcargo.DriverWorkScheduleRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverScheduleExceptionService {
	
	private final DriverScheduleExceptionRepository exceptionRepository;
    private final DriverWorkScheduleRepository driverWorkScheduleRepository;
    
    public DriverScheduleException create(
            DriverScheduleException exception
    ) {
        validate(exception);

        if (exceptionRepository
                .existsByUserIdAndExceptionDate(
                        exception.getUserId(),
                        exception.getExceptionDate()
                )) {

            throw new MainServiceException(
                    "Для водителя с ID "
                            + exception.getUserId()
                            + " уже существует изменение графика на дату "
                            + exception.getExceptionDate()
            );
        }

        validateDriverHasSchedule(
                exception.getUserId(),
                exception.getExceptionDate()
        );

        prepareFields(exception);

        exception.setId(null);

        DriverScheduleException saved =
                exceptionRepository.save(exception);

        log.info(
                "Создано исключение графика: userId={}, date={}, type={}",
                saved.getUserId(),
                saved.getExceptionDate(),
                saved.getExceptionType()
        );

        return saved;
    }

    @Transactional
    public DriverScheduleException update(
            Long id,
            DriverScheduleException request
    ) {
        DriverScheduleException existing = getById(id);

        if (request.getUserId() != null) {
            existing.setUserId(request.getUserId());
        }

        if (request.getTruckId() != null) {
            existing.setTruckId(request.getTruckId());
        }

        if (request.getExceptionDate() != null) {
            existing.setExceptionDate(
                    request.getExceptionDate()
            );
        }

        if (request.getExceptionType() != null) {
            existing.setExceptionType(
                    request.getExceptionType()
            );
        }

        if (request.getReplacementUserId() != null) {
            existing.setReplacementUserId(
                    request.getReplacementUserId()
            );
        }

        if (request.getComment() != null) {
            existing.setComment(request.getComment());
        }

        if (request.getCreatedByUserId() != null) {
            existing.setCreatedByUserId(
                    request.getCreatedByUserId()
            );
        }

        validate(existing);

        if (exceptionRepository
                .existsByUserIdAndExceptionDateAndIdNot(
                        existing.getUserId(),
                        existing.getExceptionDate(),
                        id
                )) {

            throw new MainServiceException(
                    "Для водителя с ID "
                            + existing.getUserId()
                            + " уже существует изменение графика на дату "
                            + existing.getExceptionDate()
            );
        }

        validateDriverHasSchedule(
                existing.getUserId(),
                existing.getExceptionDate()
        );

        prepareFields(existing);

        log.info(
                "Обновлено исключение рабочего графика id={}",
                id
        );

        return existing;
    }

    public DriverScheduleException getById(Long id) {
        if (id == null || id <= 0) {
            throw new MainServiceException(
                    "Не указан ID исключения рабочего графика"
            );
        }

        return exceptionRepository.findById(id)
                .orElseThrow(() -> new MainServiceException(
                        "Исключение рабочего графика с ID "
                                + id
                                + " не найдено"
                ));
    }

    public DriverScheduleException getByUserAndDate(
            Long userId,
            LocalDate date
    ) {
        validateId(userId, "ID водителя");

        if (date == null) {
            throw new MainServiceException(
                    "Не указана дата"
            );
        }

        return exceptionRepository
                .findByUserIdAndExceptionDate(
                        userId,
                        date
                )
                .orElseThrow(() -> new MainServiceException(
                        "Изменение графика водителя "
                                + userId
                                + " на дату "
                                + date
                                + " не найдено"
                ));
    }

    public List<DriverScheduleException> getByTruckAndPeriod(
            Long truckId,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        validatePeriod(truckId, dateFrom, dateTo);

        return exceptionRepository
                .findAllByTruckIdAndExceptionDateBetweenOrderByExceptionDateAsc(
                        truckId,
                        dateFrom,
                        dateTo
                );
    }

    public List<DriverScheduleException> getByUserAndPeriod(
            Long userId,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        validatePeriod(userId, dateFrom, dateTo);

        return exceptionRepository
                .findAllByUserIdAndExceptionDateBetweenOrderByExceptionDateAsc(
                        userId,
                        dateFrom,
                        dateTo
                );
    }

    public void delete(Long id) {
        DriverScheduleException exception = getById(id);

        exceptionRepository.delete(exception);

        log.info(
                "Удалено исключение рабочего графика id={}",
                id
        );
    }

    private void validate(
            DriverScheduleException exception
    ) {
        if (exception == null) {
            throw new MainServiceException(
                    "Не переданы данные изменения графика"
            );
        }

        validateId(exception.getUserId(), "ID водителя");

        if (exception.getExceptionDate() == null) {
            throw new MainServiceException(
                    "Не указана дата изменения графика"
            );
        }

        if (exception.getExceptionType() == null) {
            throw new MainServiceException(
                    "Не указан тип изменения графика"
            );
        }

        if (exception.getExceptionType()
                == DriverScheduleExceptionType.REPLACEMENT) {

            validateId(
                    exception.getReplacementUserId(),
                    "ID заменяющего водителя"
            );

            if (exception.getUserId()
                    .equals(exception.getReplacementUserId())) {

                throw new MainServiceException(
                        "Водитель не может заменять сам себя"
                );
            }
        }
    }

    private void prepareFields(
            DriverScheduleException exception
    ) {
        if (exception.getExceptionType()
                != DriverScheduleExceptionType.REPLACEMENT) {

            exception.setReplacementUserId(null);
        }

        if (exception.getComment() != null) {
            String comment = exception.getComment().trim();

            exception.setComment(
                    comment.isBlank() ? null : comment
            );
        }
    }

    private void validateDriverHasSchedule(
            Long userId,
            LocalDate date
    ) {
        boolean hasSchedule =
                !driverWorkScheduleRepository
                        .findActiveByUserIdAndDate(
                                userId,
                                date
                        )
                        .isEmpty();

        if (!hasSchedule) {
            throw new MainServiceException(
                    "У водителя с ID "
                            + userId
                            + " нет активного графика на дату "
                            + date
            );
        }
    }

    private void validatePeriod(
            Long id,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        validateId(id, "ID");

        if (dateFrom == null || dateTo == null) {
            throw new MainServiceException(
                    "Не указан период"
            );
        }

        if (dateTo.isBefore(dateFrom)) {
            throw new MainServiceException(
                    "Конечная дата не может быть раньше начальной"
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

}
