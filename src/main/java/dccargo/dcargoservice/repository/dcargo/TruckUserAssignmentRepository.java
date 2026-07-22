package dccargo.dcargoservice.repository.dcargo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.enums.TruckUserAssignmentStatus;
import dccargo.dcargoservice.enums.TruckUserAssignmentType;
import dccargo.dcargoservice.model.dcargo.TruckUserAssignment;

@Repository
public interface TruckUserAssignmentRepository extends JpaRepository<TruckUserAssignment, Long>{
	
	/**
     * Полная история закреплений по автомобилю.
     */
    List<TruckUserAssignment> findAllByTruckIdOrderByDateFromDesc(
            Long truckId
    );

    /**
     * Полная история закреплений пользователя.
     */
    List<TruckUserAssignment> findAllByUserIdOrderByDateFromDesc(
            Long userId
    );

    /**
     * Все закрепления автомобиля по типу.
     */
    List<TruckUserAssignment> findAllByTruckIdAndAssignmentTypeOrderByDateFromDesc(
            Long truckId,
            TruckUserAssignmentType assignmentType
    );

    /**
     * Активные закрепления автомобиля определённого типа.
     */
    List<TruckUserAssignment>
    findAllByTruckIdAndAssignmentTypeAndStatusOrderByDateFromDesc(
            Long truckId,
            TruckUserAssignmentType assignmentType,
            TruckUserAssignmentStatus status
    );

    /**
     * Текущее фактическое закрепление автомобиля.
     *
     * Для ACTUAL должна существовать максимум одна активная запись.
     */
    Optional<TruckUserAssignment>
    findFirstByTruckIdAndAssignmentTypeAndStatusOrderByDateFromDesc(
            Long truckId,
            TruckUserAssignmentType assignmentType,
            TruckUserAssignmentStatus status
    );

    /**
     * Все активные закрепления пользователя.
     */
    List<TruckUserAssignment>
    findAllByUserIdAndStatusOrderByDateFromDesc(
            Long userId,
            TruckUserAssignmentStatus status
    );

    /**
     * Проверка существования аналогичного активного закрепления.
     */
    boolean existsByTruckIdAndUserIdAndAssignmentTypeAndStatus(
            Long truckId,
            Long userId,
            TruckUserAssignmentType assignmentType,
            TruckUserAssignmentStatus status
    );

    /**
     * Такая же проверка при обновлении, исключая текущую запись.
     */
    boolean existsByTruckIdAndUserIdAndAssignmentTypeAndStatusAndIdNot(
            Long truckId,
            Long userId,
            TruckUserAssignmentType assignmentType,
            TruckUserAssignmentStatus status,
            Long id
    );

    /**
     * Массовая загрузка закреплений для TruckDTO.
     */
    List<TruckUserAssignment> findAllByTruckIdIn(
            Collection<Long> truckIds
    );

    /**
     * Массовая загрузка только активных закреплений для TruckDTO.
     */
    List<TruckUserAssignment> findAllByTruckIdInAndStatus(
            Collection<Long> truckIds,
            TruckUserAssignmentStatus status
    );

}
