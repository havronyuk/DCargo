package dccargo.dcargoservice.model.dcargo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "passport")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_passport")
    private Integer idPassport;

    @Column(name = "series", length = 45)
    private String series;

    @Column(name = "number", length = 45)
    private String number;

    @Column(name = "personal_number", length = 255)
    private String personalNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "issued_by", columnDefinition = "TEXT")
    private String issuedBy;

    @Column(name = "block", nullable = false)
    private Boolean block = false;

}
