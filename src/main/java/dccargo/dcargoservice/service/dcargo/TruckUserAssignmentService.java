package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import dccargo.dcargoservice.enums.TruckUserAssignmentStatus;
import dccargo.dcargoservice.enums.TruckUserAssignmentType;
import dccargo.dcargoservice.model.dcargo.TruckUserAssignment;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.repository.dcargo.TruckUserAssignmentRepository;
import dccargo.dcargoservice.repository.dcargo.UserRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TruckUserAssignmentService {
	
	private final TruckUserAssignmentRepository assignmentRepository;

    private final TruckRepository truckRepository;

    private final UserRepository userRepository;
    
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
    public TruckUserAssignment create(
            TruckUserAssignment assignment) {

        validateRequiredFields(assignment);
        validateReferences(assignment);
        validateDates(assignment.getDateFrom(), assignment.getDateTo());

        if (assignment.getStatus() == null) {
            assignment.setStatus(
                    TruckUserAssignmentStatus.ACTIVE
            );
        }

        /*
         * Запрещаем создавать одинаковое активное закрепление:
         *
         * тот же автомобиль;
         * тот же пользователь;
         * тот же тип;
         * статус ACTIVE.
         */
        if (assignment.getStatus()
                == TruckUserAssignmentStatus.ACTIVE
                && assignmentRepository
                .existsByTruckIdAndUserIdAndAssignmentTypeAndStatus(
                        assignment.getTruckId(),
                        assignment.getUserId(),
                        assignment.getAssignmentType(),
                        TruckUserAssignmentStatus.ACTIVE
                )) {

            throw new MainServiceException(
                    "Такое активное закрепление уже существует"
            );
        }

        /*
         * Если создаётся новое фактическое закрепление,
         * закрываем предыдущего фактического водителя машины.
         */
        if (assignment.getAssignmentType()
                == TruckUserAssignmentType.ACTUAL
                && assignment.getStatus()
                == TruckUserAssignmentStatus.ACTIVE) {

            completeCurrentActualAssignment(
                    assignment.getTruckId()
            );
        }

        /*
         * createdAt, updatedAt и dateFrom будут заполнены
         * в @PrePersist модели, если они не указаны.
         */

        return assignmentRepository.save(assignment);
    }

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

        validateTruckId(resultTruckId);
        validateUserId(resultUserId);
        validateDates(resultDateFrom, resultDateTo);

        if (assignment.getCreatedByUserId() != null) {
            validateUserId(assignment.getCreatedByUserId());
        }

        if (resultStatus == TruckUserAssignmentStatus.ACTIVE
                && assignmentRepository
                .existsByTruckIdAndUserIdAndAssignmentTypeAndStatusAndIdNot(
                        resultTruckId,
                        resultUserId,
                        resultType,
                        TruckUserAssignmentStatus.ACTIVE,
                        assignment.getId()
                )) {

            throw new MainServiceException(
                    "Такое активное закрепление уже существует"
            );
        }

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
     */
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
     * Отмена закрепления.
     */
    @Transactional
    public TruckUserAssignment cancel(Long id) {

        TruckUserAssignment assignment = getById(id);

        if (assignment.getStatus()
                != TruckUserAssignmentStatus.ACTIVE) {

            throw new MainServiceException(
                    "Можно отменить только активное закрепление"
            );
        }

        assignment.setStatus(
                TruckUserAssignmentStatus.CANCELLED
        );

        assignment.setDateTo(LocalDateTime.now());

        return assignmentRepository.save(assignment);
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
