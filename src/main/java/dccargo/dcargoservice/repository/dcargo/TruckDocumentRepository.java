package dccargo.dcargoservice.repository.dcargo;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import dccargo.dcargoservice.model.dcargo.TruckDocument;

@Repository
public interface TruckDocumentRepository extends JpaRepository<TruckDocument, Long>{
	
	boolean existsByDocumentNumber(String documentNumber);
	 
	List<TruckDocument> findAllByTruckIdOrderByInspectionDateDesc (Long id);
	
	List<TruckDocument> findAllByTruckIdAndStatusOrderByValidUntilDesc(
            Long truckId,
            TechnicalInspectionStatus status
    );

    List<TruckDocument> findAllByTruckIdIn(
            Collection<Long> truckIds
    );

}
