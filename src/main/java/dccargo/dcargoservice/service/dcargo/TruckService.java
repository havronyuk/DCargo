package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.enums.TruckStatus;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class TruckService {

    private final TruckRepository truckRepository;
    
    public List<Truck> getAllTruck() {
		return truckRepository.findAll();		
	}
    
    public Truck create(Truck truck) {
    	if (truckRepository.existsByRegistrationNumber(truck.getRegistrationNumber())) {
            throw new MainServiceException(
                    "Автомобиль с госномером "
                            + truck.getRegistrationNumber()
                            + " уже существует"
            );
        }
    	
    	if (truckRepository.existsByInternalId(truck.getInternalId())) {
            throw new MainServiceException(
                    "Автомобиль с внутренним id "
                            + truck.getInternalId()
                            + " уже существует"
            );
        }
    	
    	if (truckRepository.existsByInternalNumber(truck.getInternalNumber())) {
            throw new MainServiceException(
                    "Автомобиль с внутренним номером "
                            + truck.getInternalNumber()
                            + " уже существует"
            );
        }
    	
    	if (truckRepository.existsByGarageNumber(truck.getGarageNumber())) {
    		throw new MainServiceException(
    				"Автомобиль с гаражным номером "
    						+ truck.getGarageNumber()
    						+ " уже существует"
    				);
    	}
    	
    	if (truck.getVin() != null && truckRepository.existsByVin(truck.getVin())) {
            throw new MainServiceException(
                    "Автомобиль с VIN "
                            + truck.getVin()
                            + " уже существует"
            );
        }
    	
    	truck.setCreatedAt(LocalDateTime.now());  
    	truck.setStatus(TruckStatus.INACTIVE);  
    	//TODO потом добавить userCreate
    	return truckRepository.save(truck);
	}
    
    /**
     * Метод обновления машин. <br> <b>Важно: проверяет все поля вручную!</b>
     * @param truck
     * @return
     */
    @Transactional
    public Truck update(Truck truck) {
    	if(truck.getId() == null ) {
    		throw new MainServiceException("Отсутствует id в запросе");
    	}

        Truck dbTruck = truckRepository.findById(truck.getId())
                .orElseThrow(() -> new MainServiceException("Транспортное средство не найдено"));

        dbTruck.setInternalId(truck.getInternalId() != null ? truck.getInternalId() : dbTruck.getInternalId());
        dbTruck.setInternalNumber(truck.getInternalNumber() != null ? truck.getInternalNumber() : dbTruck.getInternalNumber());
        dbTruck.setGarageNumber(truck.getGarageNumber() != null ? truck.getGarageNumber() : dbTruck.getGarageNumber());
        dbTruck.setRegistrationNumber(truck.getRegistrationNumber() != null ? truck.getRegistrationNumber() : dbTruck.getRegistrationNumber());
        dbTruck.setVin(truck.getVin() != null ? truck.getVin() : dbTruck.getVin());
        dbTruck.setBrand(truck.getBrand() != null ? truck.getBrand() : dbTruck.getBrand());
        dbTruck.setModel(truck.getModel() != null ? truck.getModel() : dbTruck.getModel());
        dbTruck.setManufactureYear(truck.getManufactureYear() != null ? truck.getManufactureYear() : dbTruck.getManufactureYear());
        dbTruck.setTruckType(truck.getTruckType() != null ? truck.getTruckType() : dbTruck.getTruckType());
        dbTruck.setLengthMm(truck.getLengthMm() != null ? truck.getLengthMm() : dbTruck.getLengthMm());
        dbTruck.setWidthMm(truck.getWidthMm() != null ? truck.getWidthMm() : dbTruck.getWidthMm());
        dbTruck.setHeightMm(truck.getHeightMm() != null ? truck.getHeightMm() : dbTruck.getHeightMm());
        dbTruck.setVolumeM3(truck.getVolumeM3() != null ? truck.getVolumeM3() : dbTruck.getVolumeM3());
        dbTruck.setMaxWeightKg(truck.getMaxWeightKg() != null ? truck.getMaxWeightKg() : dbTruck.getMaxWeightKg());
        dbTruck.setMaxEuroPallets(truck.getMaxEuroPallets() != null ? truck.getMaxEuroPallets() : dbTruck.getMaxEuroPallets());
        dbTruck.setMaxFinPallets(truck.getMaxFinPallets() != null ? truck.getMaxFinPallets() : dbTruck.getMaxFinPallets());
        dbTruck.setMaxRollboxes(truck.getMaxRollboxes() != null ? truck.getMaxRollboxes() : dbTruck.getMaxRollboxes());
        dbTruck.setCommissioningDate(truck.getCommissioningDate() != null ? truck.getCommissioningDate() : dbTruck.getCommissioningDate());
        dbTruck.setDecommissioningDate(truck.getDecommissioningDate() != null ? truck.getDecommissioningDate() : dbTruck.getDecommissioningDate());
        dbTruck.setStatus(truck.getStatus() != null ? truck.getStatus() : dbTruck.getStatus());
        dbTruck.setComment(truck.getComment() != null ? truck.getComment() : dbTruck.getComment());
        dbTruck.setUpdatedAt(LocalDateTime.now());
        return truckRepository.save(dbTruck);
    }
    

}
