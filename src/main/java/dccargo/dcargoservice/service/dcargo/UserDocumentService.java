package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import dccargo.dcargoservice.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import dccargo.dcargoservice.model.dcargo.UserDocType;
import dccargo.dcargoservice.model.dcargo.UserDocument;
import dccargo.dcargoservice.repository.dcargo.UserDocTypeRepository;
import dccargo.dcargoservice.repository.dcargo.UserDocumentRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDocumentService {

	private final UserDocumentRepository userDocumentRepository;

	private final UserDocTypeRepository userDocTypeRepository;

	private final SecurityUtils securityUtils;

	/**
	 * Получить все документы пользователя.
	 */
	public List<UserDocument> getByUserId(Long userId) {
		return userDocumentRepository.findAllByUserIdOrderByInspectionDateDesc(userId);
	}

	/**
	 * Получить все документы.
	 */
	public List<UserDocument> getAll() {
		return userDocumentRepository.findAll();
	}

	/**
	 * Получить документ по ID.
	 */
	public UserDocument getById(Long id) {
		return userDocumentRepository.findById(id)
				.orElseThrow(() -> new MainServiceException(
						"Документ пользователя не найден"
				));
	}

	/**
	 * Создание документа пользователя.
	 */
	public UserDocument create(UserDocument userDocument) {

		if (userDocument.getUserId() == null) {
			throw new MainServiceException(
					"Не указан ID пользователя"
			);
		}

		if (userDocument.getDocumentTypeId() == null) {
			throw new MainServiceException(
					"Не указан ID типа документа"
			);
		}

		UserDocType userDocType = userDocTypeRepository
				.findById(userDocument.getDocumentTypeId())
				.orElseThrow(() -> new MainServiceException(
						"Тип документа пользователя с ID "
								+ userDocument.getDocumentTypeId()
								+ " не найден"
				));

		userDocument.setDocumentTypeName(userDocType.getName());

		if (userDocument.getInspectionDate() == null) {
			throw new MainServiceException(
					"Не указана дата проведения действия документа"
			);
		}

		if (userDocument.getValidUntil() == null) {
			throw new MainServiceException(
					"Не указана дата окончания действия документа"
			);
		}

		if (userDocument.getValidUntil()
				.isBefore(userDocument.getInspectionDate())) {
			throw new MainServiceException(
					"Дата окончания действия не может быть раньше даты проведения документа"
			);
		}

		if (userDocument.getDocumentNumber() != null
				&& userDocumentRepository.existsByDocumentNumber(
						userDocument.getDocumentNumber())) {
			throw new MainServiceException(
					"Документ с номером "
							+ userDocument.getDocumentNumber()
							+ " уже существует"
			);
		}

		userDocument.setStatus(TechnicalInspectionStatus.ACTIVE);
		userDocument.setCreatedByUserId(securityUtils.getCurrentUserId());
		userDocument.setCreatedByUserName(securityUtils.getCurrentUsername());
		userDocument.setFromSystem("Yard");

		return userDocumentRepository.save(userDocument);
	}

	/**
	 * Обновление документа пользователя.
	 * Изменяются только поля, переданные в запросе (не null).
	 */
	@Transactional
	public UserDocument update(UserDocument userDocument) {

		if (userDocument.getId() == null) {
			throw new MainServiceException(
					"Отсутствует ID документа в запросе"
			);
		}

		UserDocument dbDocument = userDocumentRepository.findById(userDocument.getId())
				.orElseThrow(() -> new MainServiceException(
						"Документ пользователя не найден"
				));

		if (userDocument.getDocumentNumber() != null
				&& !userDocument.getDocumentNumber()
						.equals(dbDocument.getDocumentNumber())
				&& userDocumentRepository.existsByDocumentNumber(
						userDocument.getDocumentNumber())) {
			throw new MainServiceException(
					"Документ с номером "
							+ userDocument.getDocumentNumber()
							+ " уже существует"
			);
		}

		if (userDocument.getDocumentTypeId() == null) {
			throw new MainServiceException(
					"Не указан ID типа документа"
			);
		}

		UserDocType userDocType = userDocTypeRepository
				.findById(userDocument.getDocumentTypeId())
				.orElseThrow(() -> new MainServiceException(
						"Тип документа пользователя с ID "
								+ userDocument.getDocumentTypeId()
								+ " не найден"
				));

		userDocument.setDocumentTypeName(userDocType.getName());

		dbDocument.setUserId(
				userDocument.getUserId() != null
						? userDocument.getUserId()
						: dbDocument.getUserId()
		);

		dbDocument.setInspectionDate(
				userDocument.getInspectionDate() != null
						? userDocument.getInspectionDate()
						: dbDocument.getInspectionDate()
		);

		dbDocument.setValidUntil(
				userDocument.getValidUntil() != null
						? userDocument.getValidUntil()
						: dbDocument.getValidUntil()
		);

		dbDocument.setDocumentNumber(
				userDocument.getDocumentNumber() != null
						? userDocument.getDocumentNumber()
						: dbDocument.getDocumentNumber()
		);

		dbDocument.setStatus(
				userDocument.getStatus() != null
						? userDocument.getStatus()
						: dbDocument.getStatus()
		);

		dbDocument.setComment(
				userDocument.getComment() != null
						? userDocument.getComment()
						: dbDocument.getComment()
		);

		dbDocument.setDocumentTypeId(
				userDocument.getDocumentTypeId() != null
						? userDocument.getDocumentTypeId()
						: dbDocument.getDocumentTypeId()
		);

		dbDocument.setDocumentTypeName(
				userDocument.getDocumentTypeName() != null
						? userDocument.getDocumentTypeName()
						: dbDocument.getDocumentTypeName()
		);

		if (dbDocument.getInspectionDate() != null
				&& dbDocument.getValidUntil() != null
				&& dbDocument.getValidUntil()
						.isBefore(dbDocument.getInspectionDate())) {
			throw new MainServiceException(
					"Дата окончания действия не может быть раньше даты проведения документа"
			);
		}

		dbDocument.setUpdatedAt(LocalDateTime.now());

		return userDocumentRepository.save(dbDocument);
	}

}
