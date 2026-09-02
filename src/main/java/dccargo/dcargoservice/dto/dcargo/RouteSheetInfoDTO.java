package dccargo.dcargoservice.dto.dcargo;


import lombok.Getter;
import lombok.Setter;

import dccargo.dcargoservice.model.dcargo.Refueling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RouteSheetInfoDTO {

    private Long idTruckUserAssigment;
    private Long idRouteSheet;
    private Long idOrder;
    private Long idTruck;
    private Long idUser;
    private String routeSheetStatus;
    private String workerFullName;
    private String registrationNumber;
    private String workerTabelNumber;
    private String carBrand;
    private String driverCardNumber;
    private LocalDate enterLineDate;
    private LocalDate endLineDate;
    private Integer startOdometerValue;
    private Integer endOdometerValue;

    private Double startRefWorkValue;
    private Double stopRefWorkValue;

    private Double refWorkTime;
    private Double vebastoWorkTime;

    private LocalDateTime lastPrintTime;

    private List<Refueling> refuelingList;

}
