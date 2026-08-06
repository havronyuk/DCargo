package dccargo.dcargoservice.model.dcargo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "driver_card")
public class DriverCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_driver_card")
    private Long idDriverCard;

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "number", length = 255)
    private String number;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "issued_by", columnDefinition = "TEXT")
    private String issuedBy;

    @Column(name = "categories", length = 255)
    private String categories;

    @Column(name = "block", nullable = false)
    private Boolean block = false;

    /**
     * Кто создал запись.
     */
    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    /**
     * Когда создана запись.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Каким логином создана запись.
     */
    @Column(name = "created_by_user_name")
    private String createdByUserName;

    /**
     * В какой системе создана запись.
     */
    @Column(name = "from_system")
    private String fromSystem;




}