package dccargo.dcargoservice.dto.dcargo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import dccargo.dcargoservice.enums.TruckStatus;
import dccargo.dcargoservice.model.dcargo.TruckDocument;
import dccargo.dcargoservice.model.dcargo.TruckEquipment;
import dccargo.dcargoservice.model.dcargo.TruckMileage;
import dccargo.dcargoservice.model.dcargo.TruckTire;
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
public class TruckDTO {
	
	/*
     * Основная информация об автомобиле
     */

    private Long id;

    private String internalId;

    private String internalNumber;

    private String garageNumber;

    private String registrationNumber;

    private String vin;

    private String brand;

    private String model;

    private Integer manufactureYear;

    private String truckType;

    private Integer lengthMm;

    private Integer widthMm;

    private Integer heightMm;

    private BigDecimal volumeM3;

    private BigDecimal maxWeightKg;

    private Integer maxEuroPallets;

    private Integer maxFinPallets;

    private Integer maxRollboxes;

    private LocalDate commissioningDate;

    private LocalDate decommissioningDate;

    private TruckStatus status;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /*
     * Текущий пробег
     */

    private Integer currentMileage;

    private LocalDateTime currentMileageDate;

    /*
     * Связанные объекты
     */

    private List<TruckDocument> documents;

    private List<TruckEquipment> equipment;

    private List<TruckTire> tires;

    private List<TruckMileage> mileageHistory;

}
