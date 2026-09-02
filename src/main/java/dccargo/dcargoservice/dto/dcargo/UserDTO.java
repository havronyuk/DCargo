package dccargo.dcargoservice.dto.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import dccargo.dcargoservice.model.dcargo.DriverCard;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.model.dcargo.UserDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /*
     * Основная информация о пользователе
     */

    private Long idUser;

    private String name;

    private String surname;

    private String patronymic;

    private String fullName;

    private String login;

    private String loginTelephone;

    private String telephone;

    private Boolean enablet;

    private String department;

    private String email;

    private String numYNP;

    private String loyalty;

    private Integer status;

    private Boolean block;

    private String ip;

    private LocalDateTime dateCreate;

    private Long chatId;

    private Long tabNumber;

    /*
     * Связанные объекты
     */

    private Passport passport;

    private DriverCard driverCard;

    private List<UserDocument> documents;

}