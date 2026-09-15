package dccargo.dcargoservice.dto.dcargo;

import lombok.Data;

import java.util.List;

@Data
public class CompleteRouteSheetDayRequest {

    private Long idTruckUserAssignment;
    private Integer startOdometerValue;
    private Integer endOdometerValue;
    private Double refWorkTime;
    private Double vebastoWorkTime;

    private List<RefuelingRequest> refuelings;

    @Data
    public static class RefuelingRequest {
        private Double fuelAmount;
    }
}