package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import dccargo.dcargoservice.util.SecurityUtils;
import org.springframework.stereotype.Service;

import dccargo.dcargoservice.model.dcargo.DocumentType;
import dccargo.dcargoservice.repository.dcargo.DocumentTypeRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Объект типов документов
 * типы можно только создавать, удалять нельзя
 */
@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class DocumentTypeService {
	
	private final DocumentTypeRepository documentTypeRepository;

    private final SecurityUtils securityUtils;
	
	public List<DocumentType> getAll() {		
		return documentTypeRepository.findAll();		
	}
	
	public DocumentType create(DocumentType documentType) {

	    if (documentTypeRepository.existsByName(documentType.getName())) {
	        throw new MainServiceException(
	                "Тип документа с названием \""
	                        + documentType.getName()
	                        + "\" уже существует"
	        );
	    }

	    if (documentTypeRepository.existsByCode(documentType.getCode())) {
	        throw new MainServiceException(
	                "Тип документа с кодом \""
	                        + documentType.getCode()
	                        + "\" уже существует"
	        );
	    }

	    documentType.setCreatedAt(LocalDateTime.now());
        documentType.setFromSystem("Yard");
        documentType.setCreatedByUserId(securityUtils.getCurrentUserId());
        documentType.setCreatedByUserName(securityUtils.getCurrentUsername());


	    return documentTypeRepository.save(documentType);
	}
	

}
