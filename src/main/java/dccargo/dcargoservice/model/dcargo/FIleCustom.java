package dccargo.dcargoservice.model.dcargo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "files")
public class FIleCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfiles")
    private Long idFiles;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    @Lob
    @Column(name = "data", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] data;

    @Column(name = "date_create")
    private Timestamp dateCreate;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name", columnDefinition = "TEXT")
    private String userName;

    @Column(name = "user_company_name", columnDefinition = "TEXT")
    private String userCompanyName;

    @Column(name = "user_email", columnDefinition = "TEXT")
    private String userEmail;

    @Column(name = "status")
    private Integer status;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
    
    @Column(name = "size", precision = 19, scale = 2)
    private BigDecimal size;
    
    @Column(name = "size_type", columnDefinition = "TEXT")
    private String sizeType;
    
    @Column(name = "type", columnDefinition = "TEXT")
    private String type;
    
    @Column(name = "application", columnDefinition = "TEXT")
    private String application;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
        
    @Column(name = "id_object")
    private Long idObject;  
    
}
