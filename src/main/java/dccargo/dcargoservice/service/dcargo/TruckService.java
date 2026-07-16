package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.service.dcargo.exception.TruckException;
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
    
    public Truck create(Truck truck) {
    	if (truckRepository.existsByRegistrationNumber(truck.getRegistrationNumber())) {
            throw new TruckException(
                    "Автомобиль с госномером "
                            + truck.getRegistrationNumber()
                            + " уже существует"
            );
        }
    	
    	if (truckRepository.existsByInternalId(truck.getInternalId())) {
            throw new TruckException(
                    "Автомобиль с внутренним id "
                            + truck.getInternalId()
                            + " уже существует"
            );
        }
    	
    	if (truckRepository.existsByInternalNumber(truck.getInternalNumber())) {
            throw new TruckException(
                    "Автомобиль с внутренним номером "
                            + truck.getInternalNumber()
                            + " уже существует"
            );
        }
    	
    	if (truckRepository.existsByGarageNumber(truck.getGarageNumber())) {
    		throw new TruckException(
    				"Автомобиль с гаражным номером "
    						+ truck.getGarageNumber()
    						+ " уже существует"
    				);
    	}
    	
    	if (truck.getVin() != null && truckRepository.existsByVin(truck.getVin())) {
            throw new TruckException(
                    "Автомобиль с VIN "
                            + truck.getVin()
                            + " уже существует"
            );
        }
    	
    	truck.setCreatedAt(LocalDateTime.now());  
    	//потом добавить userCreate
    	return truckRepository.save(truck);
	}
    

}
