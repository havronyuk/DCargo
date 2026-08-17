package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "user_document")
public class UserDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * ID транспортного средства.
     * Связь с Truck храним вручную, без @ManyToOne.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;


    /**
     * Дата и время начала действования документа.
     */
    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    /**
     * Дата окончания действия технического осмотра.
     */
    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    /**
     * Номер документа технического осмотра.
     */
    @Column(name = "document_number")
    private String documentNumber;

    /**
     * Статус
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TechnicalInspectionStatus status = TechnicalInspectionStatus.ACTIVE;

    /**
     * Дополнительный комментарий.
     */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /**
     * Дата создания записи.
     * Значение устанавливает база данных.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата последнего изменения записи.
     * Значение устанавливает база данных.
     */
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    /**
     * ID типа документа из справочника user_doc_type.
     */
    @Column(name = "document_type_id", nullable = false)
    private Long documentTypeId;

    /**
     * Название типа документа.
     * Например: Техосмотр, ОСАГО, КАСКО.
     */
    @Column(name = "document_type_name", nullable = false, length = 100)
    private String documentTypeName;


    /**
     * Кто создал запись.
     */
    private Long createdByUserId;


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
