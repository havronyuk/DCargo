package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import dccargo.dcargoservice.util.SecurityUtils;
import org.springframework.stereotype.Service;

import dccargo.dcargoservice.model.dcargo.UserDocType;
import dccargo.dcargoservice.repository.dcargo.UserDocTypeRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Справочник типов документов пользователей.
 * Типы можно только создавать, удалять нельзя.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDocTypeService {

	private final UserDocTypeRepository userDocTypeRepository;

	private final SecurityUtils securityUtils;

	public List<UserDocType> getAll() {
		return userDocTypeRepository.findAll();
	}

	public UserDocType create(UserDocType userDocType) {

		if (userDocTypeRepository.existsByName(userDocType.getName())) {
			throw new MainServiceException(
					"Тип документа пользователя с названием \""
							+ userDocType.getName()
							+ "\" уже существует"
			);
		}

		if (userDocTypeRepository.existsByCode(userDocType.getCode())) {
			throw new MainServiceException(
					"Тип документа пользователя с кодом \""
							+ userDocType.getCode()
							+ "\" уже существует"
			);
		}

		userDocType.setCreatedAt(LocalDateTime.now());
		userDocType.setCreatedByUserId(securityUtils.getCurrentUserId());
		userDocType.setCreatedByUserName(securityUtils.getCurrentUsername());

		return userDocTypeRepository.save(userDocType);
	}

}
