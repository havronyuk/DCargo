package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "document_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentType {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Отображаемое название.
     * Например: ОСАГО
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Технический код.
     * Например: OSAGO
     */
    @Column(nullable = false, length = 50)
    private String code;

    /**
     * Описание типа документа.
     */
    @Column(length = 500)
    private String description;

    /**
     * Кто создал запись.
     */
    private Long createdByUserId;

    /**
     * Когда создана запись.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

}
