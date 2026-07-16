package dccargo.dcargoservice.repository.dcargo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.model.dcargo.TechnicalInspection;

@Repository
public interface TechnicalInspectionRepository extends JpaRepository<TechnicalInspection, Long>{
	
	boolean existsByDocumentNumber(String documentNumber);
	 
	List<TechnicalInspection> findAllByTruckIdOrderByInspectionDateDesc (Long id);

}
