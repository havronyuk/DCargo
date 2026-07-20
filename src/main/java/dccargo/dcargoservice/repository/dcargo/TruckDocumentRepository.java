package dccargo.dcargoservice.repository.dcargo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.model.dcargo.TruckDocument;

@Repository
public interface TruckDocumentRepository extends JpaRepository<TruckDocument, Long>{
	
	boolean existsByDocumentNumber(String documentNumber);
	 
	List<TruckDocument> findAllByTruckIdOrderByInspectionDateDesc (Long id);

}
