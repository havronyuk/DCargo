package dccargo.dcargoservice.dto.dcargo;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO для закрепления двух водителей
 */
@Getter
@Setter
public class AssignTruckDriversDTO {
	
	private Long truckId;

    private Long scheduleId;

    private Long firstDriverId;

    private Long secondDriverId;

    private LocalDate cycleStartDate;

    private LocalDate dateFrom;

}
