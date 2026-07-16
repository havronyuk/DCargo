package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import dccargo.dcargoservice.model.dcargo.TechnicalInspection;
import dccargo.dcargoservice.repository.dcargo.TechnicalInspectionRepository;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class TechnicalInspectionService {
	
	private final TechnicalInspectionRepository technicalInspectionRepository;
	
	public List<TechnicalInspection> getTechnicalInspectionByIdTruck(Long id) {
		return technicalInspectionRepository.findAllByTruckIdOrderByInspectionDateDesc(id).stream().toList();		
	}
	
	/**
     * Создание технического осмотра.
     */
    public TechnicalInspection create(TechnicalInspection technicalInspection) {

        if (technicalInspection.getTruckId() == null) {
            throw new MainServiceException(
                    "Не указан ID транспортного средства"
            );
        }

        if (technicalInspection.getRegistrationNumber() == null
                || technicalInspection.getRegistrationNumber().isBlank()) {
            throw new MainServiceException(
                    "Не указан государственный номер транспортного средства"
            );
        }

        if (technicalInspection.getInspectionDate() == null) {
            throw new MainServiceException(
                    "Не указана дата проведения технического осмотра"
            );
        }

        if (technicalInspection.getValidUntil() == null) {
            throw new MainServiceException(
                    "Не указана дата окончания действия технического осмотра"
            );
        }

        if (technicalInspection.getValidUntil()
                .isBefore(technicalInspection.getInspectionDate().toLocalDate())) {
            throw new MainServiceException(
                    "Дата окончания действия не может быть раньше даты проведения ТО"
            );
        }

        if (technicalInspection.getDocumentNumber() != null
                && technicalInspectionRepository.existsByDocumentNumber(
                        technicalInspection.getDocumentNumber())) {
            throw new MainServiceException(
                    "Технический осмотр с номером документа "
                            + technicalInspection.getDocumentNumber()
                            + " уже существует"
            );
        }

        technicalInspection.setCreatedAt(LocalDateTime.now());
        technicalInspection.setStatus(TechnicalInspectionStatus.ACTIVE);

        return technicalInspectionRepository.save(technicalInspection);
    }

    /**
     * Обновление технического осмотра.
     * <br>
     * <b>Важно: изменяются только поля, переданные в запросе.</b>
     */
    @Transactional
    public TechnicalInspection update(TechnicalInspection technicalInspection) {

        if (technicalInspection.getId() == null) {
            throw new MainServiceException(
                    "Отсутствует ID технического осмотра в запросе"
            );
        }

        TechnicalInspection dbTechnicalInspection =
                technicalInspectionRepository.findById(technicalInspection.getId())
                        .orElseThrow(() -> new MainServiceException(
                                "Технический осмотр не найден"
                        ));

        if (technicalInspection.getDocumentNumber() != null
                && !technicalInspection.getDocumentNumber()
                        .equals(dbTechnicalInspection.getDocumentNumber())
                && technicalInspectionRepository.existsByDocumentNumber(
                        technicalInspection.getDocumentNumber())) {

            throw new MainServiceException(
                    "Технический осмотр с номером документа "
                            + technicalInspection.getDocumentNumber()
                            + " уже существует"
            );
        }

        dbTechnicalInspection.setTruckId(
                technicalInspection.getTruckId() != null
                        ? technicalInspection.getTruckId()
                        : dbTechnicalInspection.getTruckId()
        );

        dbTechnicalInspection.setRegistrationNumber(
                technicalInspection.getRegistrationNumber() != null
                        ? technicalInspection.getRegistrationNumber()
                        : dbTechnicalInspection.getRegistrationNumber()
        );

        dbTechnicalInspection.setInspectionDate(
                technicalInspection.getInspectionDate() != null
                        ? technicalInspection.getInspectionDate()
                        : dbTechnicalInspection.getInspectionDate()
        );

        dbTechnicalInspection.setValidUntil(
                technicalInspection.getValidUntil() != null
                        ? technicalInspection.getValidUntil()
                        : dbTechnicalInspection.getValidUntil()
        );

        dbTechnicalInspection.setMileageStartId(
                technicalInspection.getMileageStartId() != null
                        ? technicalInspection.getMileageStartId()
                        : dbTechnicalInspection.getMileageStartId()
        );

        dbTechnicalInspection.setMileageEndId(
                technicalInspection.getMileageEndId() != null
                        ? technicalInspection.getMileageEndId()
                        : dbTechnicalInspection.getMileageEndId()
        );

        dbTechnicalInspection.setDocumentNumber(
                technicalInspection.getDocumentNumber() != null
                        ? technicalInspection.getDocumentNumber()
                        : dbTechnicalInspection.getDocumentNumber()
        );

        dbTechnicalInspection.setStatus(
                technicalInspection.getStatus() != null
                        ? technicalInspection.getStatus()
                        : dbTechnicalInspection.getStatus()
        );

        dbTechnicalInspection.setComment(
                technicalInspection.getComment() != null
                        ? technicalInspection.getComment()
                        : dbTechnicalInspection.getComment()
        );

        if (dbTechnicalInspection.getInspectionDate() != null
                && dbTechnicalInspection.getValidUntil() != null
                && dbTechnicalInspection.getValidUntil()
                        .isBefore(dbTechnicalInspection.getInspectionDate().toLocalDate())) {

            throw new MainServiceException(
                    "Дата окончания действия не может быть раньше даты проведения ТО"
            );
        }

        dbTechnicalInspection.setUpdatedAt(LocalDateTime.now());

        return technicalInspectionRepository.save(dbTechnicalInspection);
    }

    /**
     * Получить все технические осмотры.
     */
    public List<TechnicalInspection> getAll() {
        return technicalInspectionRepository.findAll();
    }

    /**
     * Получить технический осмотр по ID.
     */
    public TechnicalInspection getById(Long id) {
        return technicalInspectionRepository.findById(id)
                .orElseThrow(() -> new MainServiceException(
                        "Технический осмотр не найден"
                ));
    }

    /**
     * Получить все технические осмотры автомобиля.
     */
    public List<TechnicalInspection> getByTruckId(Long truckId) {
        return technicalInspectionRepository
                .findAllByTruckIdOrderByInspectionDateDesc(truckId);
        
    }

}
