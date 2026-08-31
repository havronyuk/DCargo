package dccargo.dcargoservice.service.dcargo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dccargo.dcargoservice.dto.dcargo.UserDTO;
import dccargo.dcargoservice.dto.dcargo.mapper.UserDTOMapper;
import dccargo.dcargoservice.model.dcargo.DriverCard;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.model.dcargo.UserDocument;
import dccargo.dcargoservice.repository.dcargo.DriverCardRepository;
import dccargo.dcargoservice.repository.dcargo.PassportRepository;
import dccargo.dcargoservice.repository.dcargo.UserDocumentRepository;
import dccargo.dcargoservice.repository.dcargo.UserRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDTOService {

	private final UserRepository userRepository;
	private final PassportRepository passportRepository;
	private final DriverCardRepository driverCardRepository;
	private final UserDocumentRepository userDocumentRepository;

	private final UserDTOMapper userDTOMapper;

	/**
	 * Получить одного пользователя со всеми связанными объектами.
	 */
	@Transactional
	public UserDTO getById(Long userId) {

		User user = userRepository.findByIdUser(userId)
				.orElseThrow(() -> new MainServiceException(
						"Пользователь с ID " + userId + " не найден"
				));

		Passport passport = passportRepository.findByIdUserAndBlock(userId, false);

		DriverCard driverCard = driverCardRepository
				.findByIdUserAndBlock(userId, false)
				.orElse(null);

		List<UserDocument> documents =
				userDocumentRepository.findAllByUserIdOrderByInspectionDateDesc(userId);

		return userDTOMapper.toDTO(
				user,
				passport,
				driverCard,
				documents
		);
	}

	/**
	 * Получить всех пользователей со всеми связанными объектами.
	 * <br>
	 * Выполняется фиксированное количество запросов:
	 * 1 — пользователи;
	 * 1 — паспорта;
	 * 1 — водительские удостоверения;
	 * 1 — документы пользователей.
	 */
	@Transactional
	public List<UserDTO> getAll() {

		List<User> users = userRepository.findAll();

		if (users.isEmpty()) {
			return List.of();
		}

		List<Long> userIds = users.stream()
				.map(User::getIdUser)
				.toList();

		List<Passport> passports =
				passportRepository.findAllByIdUserIn(userIds);

		List<DriverCard> driverCards =
				driverCardRepository.findAllByIdUserIn(userIds);

		List<UserDocument> documents =
				userDocumentRepository.findAllByUserIdIn(userIds);

		Map<Long, Passport> passportByUser = passports.stream()
				.filter(p -> !Boolean.TRUE.equals(p.getBlock()))
				.collect(Collectors.toMap(
						Passport::getIdUser,
						Function.identity(),
						(existing, replacement) -> existing
				));

		Map<Long, DriverCard> driverCardByUser = driverCards.stream()
				.filter(c -> !Boolean.TRUE.equals(c.getBlock()))
				.collect(Collectors.toMap(
						DriverCard::getIdUser,
						Function.identity(),
						(existing, replacement) -> existing
				));

		Map<Long, List<UserDocument>> documentsByUser = documents.stream()
				.collect(Collectors.groupingBy(
						UserDocument::getUserId
				));

		return users.stream()
				.map(user -> userDTOMapper.toDTO(
						user,
						passportByUser.get(user.getIdUser()),
						driverCardByUser.get(user.getIdUser()),
						documentsByUser.getOrDefault(
								user.getIdUser(),
								List.of()
						)
				))
				.toList();
	}

}