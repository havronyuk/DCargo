package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import dccargo.dcargoservice.model.dcargo.DocumentType;
import dccargo.dcargoservice.model.dcargo.TruckDocument;
import dccargo.dcargoservice.repository.dcargo.DocumentTypeRepository;
import dccargo.dcargoservice.repository.dcargo.TruckDocumentRepository;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class TruckDocumentService {
	
	private final TruckDocumentRepository truckDocumentRepository;
	
	private final DocumentTypeRepository documentTypeRepository;
	
	public List<TruckDocument> getTechnicalInspectionByIdTruck(Long id) {
		return truckDocumentRepository.findAllByTruckIdOrderByInspectionDateDesc(id).stream().toList();		
	}
	
	/**
     * Создание технического осмотра.
     */
    public TruckDocument create(TruckDocument truckDocument) {

        if (truckDocument.getTruckId() == null) {
            throw new MainServiceException(
                    "Не указан ID транспортного средства"
            );
        }
        
        if (truckDocument.getDocumentTypeId() == null) {
            throw new MainServiceException(
                    "Не указан ID типа документа"
            );
        }

        DocumentType documentType = documentTypeRepository
                .findById(truckDocument.getDocumentTypeId())
                .orElseThrow(() -> new MainServiceException(
                        "Тип документа с ID "
                                + truckDocument.getDocumentTypeId()
                                + " не найден"
                ));

        // Название берём из справочника, а не из запроса
        truckDocument.setDocumentTypeName(documentType.getName());

        if (truckDocument.getRegistrationNumber() == null
                || truckDocument.getRegistrationNumber().isBlank()) {
            throw new MainServiceException(
                    "Не указан государственный номер транспортного средства"
            );
        }

        if (truckDocument.getInspectionDate() == null) {
            throw new MainServiceException(
                    "Не указана дата проведения действия документа"
            );
        }

        if (truckDocument.getValidUntil() == null) {
            throw new MainServiceException(
                    "Не указана дата окончания действия документа"
            );
        }

        if (truckDocument.getValidUntil()
                .isBefore(truckDocument.getInspectionDate().toLocalDate())) {
            throw new MainServiceException(
                    "Дата окончания действия не может быть раньше даты проведения документа"
            );
        }

        if (truckDocument.getDocumentNumber() != null
                && truckDocumentRepository.existsByDocumentNumber(
                        truckDocument.getDocumentNumber())) {
            throw new MainServiceException(
                    "Объект с номером документа "
                            + truckDocument.getDocumentNumber()
                            + " уже существует"
            );
        }

        truckDocument.setCreatedAt(LocalDateTime.now());
        truckDocument.setStatus(TechnicalInspectionStatus.ACTIVE);
        
        //TODO добавить юзера и фиксацию объекта пробега

        return truckDocumentRepository.save(truckDocument);
    }

    /**
     * Обновление технического осмотра.
     * <br>
     * <b>Важно: изменяются только поля, переданные в запросе.</b>
     */
    @Transactional
    public TruckDocument update(TruckDocument truckDocument) {

        if (truckDocument.getId() == null) {
            throw new MainServiceException(
                    "Отсутствует ID документа в запросе"
            );
        }

        TruckDocument dbTechnicalInspection =
                truckDocumentRepository.findById(truckDocument.getId())
                        .orElseThrow(() -> new MainServiceException(
                                "Документ не найден"
                        ));

        if (truckDocument.getDocumentNumber() != null
                && !truckDocument.getDocumentNumber()
                        .equals(dbTechnicalInspection.getDocumentNumber())
                && truckDocumentRepository.existsByDocumentNumber(
                        truckDocument.getDocumentNumber())) {

            throw new MainServiceException(
                    "Документ с номером документа "
                            + truckDocument.getDocumentNumber()
                            + " уже существует"
            );
        }
        
        if (truckDocument.getDocumentTypeId() == null) {
            throw new MainServiceException(
                    "Не указан ID типа документа"
            );
        }

        DocumentType documentType = documentTypeRepository
                .findById(truckDocument.getDocumentTypeId())
                .orElseThrow(() -> new MainServiceException(
                        "Тип документа с ID "
                                + truckDocument.getDocumentTypeId()
                                + " не найден"
                ));

        // Название берём из справочника, а не из запроса
        truckDocument.setDocumentTypeName(documentType.getName());

        dbTechnicalInspection.setTruckId(
                truckDocument.getTruckId() != null
                        ? truckDocument.getTruckId()
                        : dbTechnicalInspection.getTruckId()
        );

        dbTechnicalInspection.setRegistrationNumber(
                truckDocument.getRegistrationNumber() != null
                        ? truckDocument.getRegistrationNumber()
                        : dbTechnicalInspection.getRegistrationNumber()
        );

        dbTechnicalInspection.setInspectionDate(
                truckDocument.getInspectionDate() != null
                        ? truckDocument.getInspectionDate()
                        : dbTechnicalInspection.getInspectionDate()
        );

        dbTechnicalInspection.setValidUntil(
                truckDocument.getValidUntil() != null
                        ? truckDocument.getValidUntil()
                        : dbTechnicalInspection.getValidUntil()
        );

        dbTechnicalInspection.setMileageStartId(
                truckDocument.getMileageStartId() != null
                        ? truckDocument.getMileageStartId()
                        : dbTechnicalInspection.getMileageStartId()
        );

        dbTechnicalInspection.setMileageEndId(
                truckDocument.getMileageEndId() != null
                        ? truckDocument.getMileageEndId()
                        : dbTechnicalInspection.getMileageEndId()
        );

        dbTechnicalInspection.setDocumentNumber(
                truckDocument.getDocumentNumber() != null
                        ? truckDocument.getDocumentNumber()
                        : dbTechnicalInspection.getDocumentNumber()
        );

        dbTechnicalInspection.setStatus(
                truckDocument.getStatus() != null
                        ? truckDocument.getStatus()
                        : dbTechnicalInspection.getStatus()
        );

        dbTechnicalInspection.setComment(
                truckDocument.getComment() != null
                        ? truckDocument.getComment()
                        : dbTechnicalInspection.getComment()
        );

        if (dbTechnicalInspection.getInspectionDate() != null
                && dbTechnicalInspection.getValidUntil() != null
                && dbTechnicalInspection.getValidUntil()
                        .isBefore(dbTechnicalInspection.getInspectionDate().toLocalDate())) {

            throw new MainServiceException(
                    "Дата окончания действия не может быть раньше даты проведения документа"
            );
        }

        dbTechnicalInspection.setUpdatedAt(LocalDateTime.now());

        return truckDocumentRepository.save(dbTechnicalInspection);
    }

    /**
     * Получить все технические осмотры.
     */
    public List<TruckDocument> getAll() {
        return truckDocumentRepository.findAll();
    }

    /**
     * Получить технический осмотр по ID.
     */
    public TruckDocument getById(Long id) {
        return truckDocumentRepository.findById(id)
                .orElseThrow(() -> new MainServiceException(
                        "Документ не найден"
                ));
    }

    /**
     * Получить все технические осмотры автомобиля.
     */
    public List<TruckDocument> getByTruckId(Long truckId) {
        return truckDocumentRepository
                .findAllByTruckIdOrderByInspectionDateDesc(truckId);
        
    }

}
