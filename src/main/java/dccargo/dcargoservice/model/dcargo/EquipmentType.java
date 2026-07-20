package dccargo.dcargoservice.model.dcargo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "equipment_type"
)
public class EquipmentType {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Отображаемое название.
     * Например: АКБ
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Технический код.
     * Например: BATTERY
     */
    @Column(nullable = false, length = 50)
    private String code;

    /**
     * Описание.
     */
    @Column(length = 500)
    private String description;

    /**
     * Активен ли тип оборудования.
     */
    @Column(nullable = false)
    private Boolean active;

    /**
     * Кто создал запись.
     */
    private Long createdByUserId;

    /**
     * Дата создания.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

}
