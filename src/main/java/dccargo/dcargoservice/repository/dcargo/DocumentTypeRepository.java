package dccargo.dcargoservice.repository.dcargo;

import org.springframework.data.jpa.repository.JpaRepository;

import dccargo.dcargoservice.model.dcargo.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
	
	public boolean existsByName(String name);
	
	public boolean existsByCode(String code);

}
