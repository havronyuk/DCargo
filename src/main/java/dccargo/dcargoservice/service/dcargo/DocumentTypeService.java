package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import dccargo.dcargoservice.model.dcargo.DocumentType;
import dccargo.dcargoservice.repository.dcargo.DocumentTypeRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class DocumentTypeService {
	
	private final DocumentTypeRepository documentTypeRepository;
	
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

	    // TODO: После внедрения авторизации
	    // documentType.setCreatedByUserId(SecurityUtils.getCurrentUserId());

	    return documentTypeRepository.save(documentType);
	}
	

}
