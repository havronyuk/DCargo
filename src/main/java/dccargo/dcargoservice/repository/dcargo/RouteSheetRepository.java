package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.enums.RouteSheetStatus;
import dccargo.dcargoservice.model.dcargo.RouteSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RouteSheetRepository extends JpaRepository<RouteSheet, Long> {

    List<RouteSheet> findByIdRouteSheetIn(List<Long> ids);

    RouteSheet findByIdRouteSheet(Long idRouteSheet);



    List<RouteSheet> findAllByIdTruckUserAssignmentIn(List<Long> ids);

    List<RouteSheet> findAllByIdTruckUserAssignmentInAndStatusNot(List<Long> ids,RouteSheetStatus status);


    boolean existsByIdTruckUserAssignmentAndStatus(Long idTruckUserAssigment, RouteSheetStatus routeSheetStatus);

    boolean existsByIdOrderAndIdTruckUserAssignment(Long idOrder, Long idTruckUserAssigment);

    RouteSheet findByIdTruckUserAssignmentAndStatus(Long idTruckUserAssigment,RouteSheetStatus routeSheetStatus );

    RouteSheet findByIdTruckUserAssignment(Long idTruckUserAssigment);

    @Modifying
    @Query("UPDATE RouteSheet r SET r.lastPrintTime = :lastPrintTime WHERE r.idRouteSheet = :idRouteSheet")
    int updateLastPrintTime(
            @Param("idRouteSheet") Long idRouteSheet,
            @Param("lastPrintTime") LocalDateTime lastPrintTime
    );

    @Query("""
    SELECT COALESCE(SUM(r.refWorkTime), 0)
    FROM RouteSheet r
    WHERE r.idTruck = :idTruck
      AND r.idRouteSheet <> :idRouteSheet
""")
    Double sumRefWorkTimeByTruckExceptRouteSheet(
            @Param("idTruck") Long idTruck,
            @Param("idRouteSheet") Long idRouteSheet
    );


}
