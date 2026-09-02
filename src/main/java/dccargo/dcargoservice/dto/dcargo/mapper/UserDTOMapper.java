package dccargo.dcargoservice.dto.dcargo.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dccargo.dcargoservice.dto.dcargo.UserDTO;
import dccargo.dcargoservice.model.dcargo.DriverCard;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.model.dcargo.UserDocument;

@Component
public class UserDTOMapper {

	public UserDTO toDTO(
			User user,
			Passport passport,
			DriverCard driverCard,
			List<UserDocument> documents
	) {

		return UserDTO.builder()
				.idUser(user.getIdUser())
				.name(user.getName())
				.surname(user.getSurname())
				.patronymic(user.getPatronymic())
				.fullName(user.getFullName())
				.login(user.getLogin())
				.loginTelephone(user.getLoginTelephone())
				.telephone(user.getTelephone())
				.enablet(user.getEnablet())
				.department(user.getDepartment())
				.email(user.getEmail())
				.numYNP(user.getNumYNP())
				.loyalty(user.getLoyalty())
				.status(user.getStatus())
				.block(user.getBlock())
				.ip(user.getIp())
				.dateCreate(user.getDateCreate())
				.chatId(user.getChatId())
				.tabNumber(user.getTabNumber())
				.passport(passport)
				.driverCard(driverCard)
				.documents(documents)
				.build();
	}

}