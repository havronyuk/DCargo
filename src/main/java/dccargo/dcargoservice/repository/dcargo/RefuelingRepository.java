package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.Refueling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefuelingRepository extends JpaRepository<Refueling,Long> {

    Refueling findByIdRefueling(Long idRefueling);

    List<Refueling> findByIdRouteSheet(Long idRouteSheet);

    List<Refueling> findByIdRouteSheetIn(List<Long> idRouteSheets);

}
