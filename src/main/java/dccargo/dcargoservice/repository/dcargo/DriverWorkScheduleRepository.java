package dccargo.dcargoservice.repository.dcargo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.model.dcargo.DriverWorkSchedule;

@Repository
public interface DriverWorkScheduleRepository extends JpaRepository<DriverWorkSchedule, Long>{
	
	List<DriverWorkSchedule>
    findAllByTruckIdOrderByDateFromDesc(Long truckId);

    List<DriverWorkSchedule>
    findAllByUserIdOrderByDateFromDesc(Long userId);

    List<DriverWorkSchedule>
    findAllByTruckIdAndStatusOrderByDateFromDesc(
            Long truckId,
            String status
    );

    List<DriverWorkSchedule>
    findAllByUserIdAndStatusOrderByDateFromDesc(
            Long userId,
            String status
    );

    Optional<DriverWorkSchedule>
    findFirstByUserIdAndStatusOrderByDateFromDesc(
            Long userId,
            String status
    );

    @Query("""
            SELECT d
            FROM DriverWorkSchedule d
            WHERE d.truckId = :truckId
              AND d.status = 'ACTIVE'
              AND d.dateFrom <= :date
              AND (d.dateTo IS NULL OR d.dateTo >= :date)
            ORDER BY d.cycleOffset ASC
            """)
    List<DriverWorkSchedule> findActiveByTruckIdAndDate(
            @Param("truckId") Long truckId,
            @Param("date") LocalDate date
    );

    @Query("""
            SELECT d
            FROM DriverWorkSchedule d
            WHERE d.userId = :userId
              AND d.status = 'ACTIVE'
              AND d.dateFrom <= :date
              AND (d.dateTo IS NULL OR d.dateTo >= :date)
            ORDER BY d.dateFrom DESC
            """)
    List<DriverWorkSchedule> findActiveByUserIdAndDate(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );

    @Query("""
            SELECT d
            FROM DriverWorkSchedule d
            WHERE d.truckId = :truckId
              AND d.dateFrom <= :dateTo
              AND (d.dateTo IS NULL OR d.dateTo >= :dateFrom)
            ORDER BY d.dateFrom ASC
            """)
    List<DriverWorkSchedule> findAllForTruckAndPeriod(
            @Param("truckId") Long truckId,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo
    );

    @Query("""
            SELECT COUNT(d)
            FROM DriverWorkSchedule d
            WHERE d.userId = :userId
              AND d.id <> COALESCE(:excludedId, -1)
              AND d.dateFrom <= :dateTo
              AND (d.dateTo IS NULL OR d.dateTo >= :dateFrom)
            """)
    long countUserScheduleIntersections(
            @Param("userId") Long userId,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("excludedId") Long excludedId
    );

    @Modifying
    @Query("""
            UPDATE DriverWorkSchedule d
            SET d.status = 'INACTIVE',
                d.dateTo = :dateTo
            WHERE d.truckId = :truckId
              AND d.status = 'ACTIVE'
            """)
    int closeActiveSchedulesByTruckId(
            @Param("truckId") Long truckId,
            @Param("dateTo") LocalDate dateTo
    );

}
