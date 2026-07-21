package dccargo.dcargoservice.dto.dcargo.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dccargo.dcargoservice.dto.dcargo.TruckDTO;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.model.dcargo.TruckDocument;
import dccargo.dcargoservice.model.dcargo.TruckEquipment;
import dccargo.dcargoservice.model.dcargo.TruckMileage;
import dccargo.dcargoservice.model.dcargo.TruckTire;

@Component
public class TruckDTOMapper {
	
	 public TruckDTO toDTO(
	            Truck truck,
	            List<TruckDocument> documents,
	            List<TruckEquipment> equipment,
	            List<TruckTire> tires,
	            List<TruckMileage> mileageHistory
	    ) {

	        TruckMileage currentMileage = mileageHistory.isEmpty()
	                ? null
	                : mileageHistory.get(0);

	        return TruckDTO.builder()
	                .id(truck.getId())
	                .internalId(truck.getInternalId())
	                .internalNumber(truck.getInternalNumber())
	                .garageNumber(truck.getGarageNumber())
	                .registrationNumber(truck.getRegistrationNumber())
	                .vin(truck.getVin())
	                .brand(truck.getBrand())
	                .model(truck.getModel())
	                .manufactureYear(truck.getManufactureYear())
	                .truckType(truck.getTruckType())
	                .lengthMm(truck.getLengthMm())
	                .widthMm(truck.getWidthMm())
	                .heightMm(truck.getHeightMm())
	                .volumeM3(truck.getVolumeM3())
	                .maxWeightKg(truck.getMaxWeightKg())
	                .maxEuroPallets(truck.getMaxEuroPallets())
	                .maxFinPallets(truck.getMaxFinPallets())
	                .maxRollboxes(truck.getMaxRollboxes())
	                .commissioningDate(truck.getCommissioningDate())
	                .decommissioningDate(truck.getDecommissioningDate())
	                .status(truck.getStatus())
	                .comment(truck.getComment())
	                .createdAt(truck.getCreatedAt())
	                .updatedAt(truck.getUpdatedAt())

	                .currentMileage(
	                        currentMileage == null
	                                ? null
	                                : currentMileage.getMileage()
	                )
	                .currentMileageDate(
	                        currentMileage == null
	                                ? null
	                                : currentMileage.getMileageDate()
	                )

	                .documents(documents)
	                .equipment(equipment)
	                .tires(tires)
	                .mileageHistory(mileageHistory)
	                .build();
	    }

}
