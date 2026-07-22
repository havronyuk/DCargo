package dccargo.dcargoservice.model.dcargo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "surname", length = 255)
    private String surname;

    @Column(name = "patronymic", length = 255)
    private String patronymic;

    @Column(name = "login", length = 255)
    private String login;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "login_telephone", length = 255)
    private String loginTelephone;

    @Column(name = "telephone", length = 255)
    private String telephone;

    @Column(name = "enablet", nullable = false)
    private Boolean enablet = true;

    @Column(name = "department", length = 255)
    private String department;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "numYNP", length = 255)
    private String numYNP;

    @Column(name = "loyalty", length = 255)
    private String loyalty;

    @Column(name = "status")
    private Integer status;

    @Column(name = "block", nullable = false)
    private Boolean block = false;

    @Column(name = "ip", columnDefinition = "TEXT")
    private String ip;

    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @Column(name = "chat_id")
    private Long chatId;

}
