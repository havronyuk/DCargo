package dccargo.dcargoservice.repository.dcargo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.model.dcargo.TechnicalInspection;

@Repository
public interface TechnicalInspectionRepository extends JpaRepository<TechnicalInspection, Long>{
	
	boolean existsByDocumentNumber(String documentNumber);
	 
	Optional<TechnicalInspection> findAllByTruckIdOrderByInspectionDateDesc (Long id);

}
