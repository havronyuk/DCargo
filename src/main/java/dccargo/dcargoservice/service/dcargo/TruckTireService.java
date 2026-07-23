package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dccargo.dcargoservice.enums.TireStatus;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.model.dcargo.TruckTire;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.repository.dcargo.TruckTireRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TruckTireService {
	
	private final TruckTireRepository truckTireRepository;

    private final TruckRepository truckRepository;

    public List<TruckTire> getAllTruckTire() {
        return truckTireRepository.findAll();
    }

    public List<TruckTire> getByTruckId(Long truckId) {
        return truckTireRepository.findByTruckId(truckId);
    }

    public TruckTire create(TruckTire truckTire) {

        if (truckTire.getInventoryNumber() != null
                && truckTireRepository.existsByInventoryNumber(
                        truckTire.getInventoryNumber())) {

            throw new MainServiceException(
                    "Шина с инвентарным номером "
                            + truckTire.getInventoryNumber()
                            + " уже существует");
        }

        if (truckTire.getSerialNumber() != null
                && truckTireRepository.existsBySerialNumber(
                        truckTire.getSerialNumber())) {

            throw new MainServiceException(
                    "Шина с серийным номером "
                            + truckTire.getSerialNumber()
                            + " уже существует");
        }

        if (truckTire.getTruckId() == null) {
            throw new MainServiceException("Не указан TruckId");
        }

        Truck truck = truckRepository.findById(truckTire.getTruckId())
                .orElseThrow(() -> new MainServiceException(
                        "Транспортное средство не найдено"));

        truckTire.setRegistrationNumber(truck.getRegistrationNumber());

        truckTire.setCreatedAt(LocalDateTime.now());
        truckTire.setStatus(TireStatus.INSTALLED);

        // TODO автоматически получать пробег автомобиля И User
        // truckTire.setMileageStartId(...)
        // truckTire.setMileageStartValue(...)

        return truckTireRepository.save(truckTire);
    }

    @Transactional
    public TruckTire update(TruckTire truckTire) {

        if (truckTire.getId() == null) {
            throw new MainServiceException("Отсутствует id в запросе");
        }

        TruckTire dbTire = truckTireRepository.findById(truckTire.getId())
                .orElseThrow(() -> new MainServiceException("Шина не найдена"));

        if (truckTire.getInventoryNumber() != null
                && truckTireRepository.existsByInventoryNumberAndIdNot(
                        truckTire.getInventoryNumber(),
                        truckTire.getId())) {

            throw new MainServiceException(
                    "Шина с инвентарным номером "
                            + truckTire.getInventoryNumber()
                            + " уже существует");
        }

        if (truckTire.getSerialNumber() != null
                && truckTireRepository.existsBySerialNumberAndIdNot(
                        truckTire.getSerialNumber(),
                        truckTire.getId())) {

            throw new MainServiceException(
                    "Шина с серийным номером "
                            + truckTire.getSerialNumber()
                            + " уже существует");
        }

        if (truckTire.getTruckId() != null) {

            Truck truck = truckRepository.findById(truckTire.getTruckId())
                    .orElseThrow(() -> new MainServiceException(
                            "Транспортное средство не найдено"));
            dbTire.setTruckId(truck.getId());
            dbTire.setRegistrationNumber(truck.getRegistrationNumber());
        }else {
        	dbTire.setTruckId(null);
            dbTire.setRegistrationNumber(null);
        }
        
        
        dbTire.setInventoryNumber(
                truckTire.getInventoryNumber() != null
                        ? truckTire.getInventoryNumber()
                        : dbTire.getInventoryNumber());

        dbTire.setSerialNumber(
                truckTire.getSerialNumber() != null
                        ? truckTire.getSerialNumber()
                        : dbTire.getSerialNumber());

        dbTire.setBrand(
                truckTire.getBrand() != null
                        ? truckTire.getBrand()
                        : dbTire.getBrand());

        dbTire.setModel(
                truckTire.getModel() != null
                        ? truckTire.getModel()
                        : dbTire.getModel());

        dbTire.setTireType(
                truckTire.getTireType() != null
                        ? truckTire.getTireType()
                        : dbTire.getTireType());

        dbTire.setSeasonType(
                truckTire.getSeasonType() != null
                        ? truckTire.getSeasonType()
                        : dbTire.getSeasonType());

        dbTire.setWidth(
                truckTire.getWidth() != null
                        ? truckTire.getWidth()
                        : dbTire.getWidth());

        dbTire.setProfile(
                truckTire.getProfile() != null
                        ? truckTire.getProfile()
                        : dbTire.getProfile());

        dbTire.setDiameter(
                truckTire.getDiameter() != null
                        ? truckTire.getDiameter()
                        : dbTire.getDiameter());

        dbTire.setManufactureDate(
                truckTire.getManufactureDate() != null
                        ? truckTire.getManufactureDate()
                        : dbTire.getManufactureDate());

        dbTire.setPurchaseDate(
                truckTire.getPurchaseDate() != null
                        ? truckTire.getPurchaseDate()
                        : dbTire.getPurchaseDate());

        dbTire.setInstallationDate(
                truckTire.getInstallationDate() != null
                        ? truckTire.getInstallationDate()
                        : dbTire.getInstallationDate());

        dbTire.setRemovalDate(
                truckTire.getRemovalDate() != null
                        ? truckTire.getRemovalDate()
                        : dbTire.getRemovalDate());

        dbTire.setAxleNumber(
                truckTire.getAxleNumber() != null
                        ? truckTire.getAxleNumber()
                        : dbTire.getAxleNumber());

        dbTire.setPosition(
                truckTire.getPosition() != null
                        ? truckTire.getPosition()
                        : dbTire.getPosition());

        dbTire.setMileageStartId(
                truckTire.getMileageStartId() != null
                        ? truckTire.getMileageStartId()
                        : dbTire.getMileageStartId());

        dbTire.setMileageStartValue(
                truckTire.getMileageStartValue() != null
                        ? truckTire.getMileageStartValue()
                        : dbTire.getMileageStartValue());

        dbTire.setMileageEndId(
                truckTire.getMileageEndId() != null
                        ? truckTire.getMileageEndId()
                        : dbTire.getMileageEndId());

        dbTire.setMileageEndValue(
                truckTire.getMileageEndValue() != null
                        ? truckTire.getMileageEndValue()
                        : dbTire.getMileageEndValue());

        dbTire.setTreadDepthStart(
                truckTire.getTreadDepthStart() != null
                        ? truckTire.getTreadDepthStart()
                        : dbTire.getTreadDepthStart());

        dbTire.setTreadDepthCurrent(
                truckTire.getTreadDepthCurrent() != null
                        ? truckTire.getTreadDepthCurrent()
                        : dbTire.getTreadDepthCurrent());

        dbTire.setPressure(
                truckTire.getPressure() != null
                        ? truckTire.getPressure()
                        : dbTire.getPressure());

        dbTire.setStatus(
                truckTire.getStatus() != null
                        ? truckTire.getStatus()
                        : dbTire.getStatus());

        dbTire.setComment(
                truckTire.getComment() != null
                        ? truckTire.getComment()
                        : dbTire.getComment());

        dbTire.setUpdatedAt(LocalDateTime.now());
   
        return truckTireRepository.save(dbTire);
    }

}
