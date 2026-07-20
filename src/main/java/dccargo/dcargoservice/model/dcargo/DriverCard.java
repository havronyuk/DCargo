package dccargo.dcargoservice.model.dcargo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "driver_card")
public class DriverCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_driver_card")
    private Integer idDriverCard;

    @Column(name = "id_user")
    private Integer idUser;

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


}