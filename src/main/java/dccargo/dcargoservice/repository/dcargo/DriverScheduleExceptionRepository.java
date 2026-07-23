package dccargo.dcargoservice.repository.dcargo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dccargo.dcargoservice.enums.DriverScheduleExceptionType;
import dccargo.dcargoservice.model.dcargo.DriverScheduleException;

public interface DriverScheduleExceptionRepository extends JpaRepository<DriverScheduleException, Long> {
	
	Optional<DriverScheduleException>
    findByUserIdAndExceptionDate(
            Long userId,
            LocalDate exceptionDate
    );

    boolean existsByUserIdAndExceptionDate(
            Long userId,
            LocalDate exceptionDate
    );

    boolean existsByUserIdAndExceptionDateAndIdNot(
            Long userId,
            LocalDate exceptionDate,
            Long id
    );

    List<DriverScheduleException>
    findAllByTruckIdAndExceptionDateBetweenOrderByExceptionDateAsc(
            Long truckId,
            LocalDate dateFrom,
            LocalDate dateTo
    );

    List<DriverScheduleException>
    findAllByUserIdAndExceptionDateBetweenOrderByExceptionDateAsc(
            Long userId,
            LocalDate dateFrom,
            LocalDate dateTo
    );

    List<DriverScheduleException>
    findAllByExceptionTypeAndExceptionDateBetweenOrderByExceptionDateAsc(
            DriverScheduleExceptionType exceptionType,
            LocalDate dateFrom,
            LocalDate dateTo
    );

}
