package dccargo.dcargoservice.dto.dcargo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TruckAndUserDTO {

    private Long idTruckUserAssigment;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Long idUser;
    private String name;
    private String surname;
    private String patronymic;
    private String loginTelephone;
    private Long idTruck;
    private String registrationNumber;
    private String model;
    private BigDecimal maxWeightKg;
    private Integer maxEuroPallets;


}
