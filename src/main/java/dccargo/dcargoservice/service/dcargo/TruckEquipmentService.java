package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import dccargo.dcargoservice.enums.TruckEquipmentStatus;
import dccargo.dcargoservice.model.dcargo.EquipmentType;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.model.dcargo.TruckEquipment;
import dccargo.dcargoservice.repository.dcargo.TruckEquipmentRepository;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TruckEquipmentService {

    private final TruckEquipmentRepository truckEquipmentRepository;
    private final EquipmentTypeService equipmentTypeService;
    
    private final TruckRepository truckRepository;

    public List<TruckEquipment> getAllTruckEquipment() {
        return truckEquipmentRepository.findAll();
    }
    
    public List<TruckEquipment> getByTruckId(Long truckId) {
        return truckEquipmentRepository.findByTruckId(truckId);
    }

    public TruckEquipment create(TruckEquipment truckEquipment) {

        if (truckEquipment.getInventoryNumber() != null &&
                truckEquipmentRepository.existsByInventoryNumber(truckEquipment.getInventoryNumber())) {

            throw new MainServiceException(
                    "Оборудование с инвентарным номером "
                            + truckEquipment.getInventoryNumber()
                            + " уже существует"
            );
        }

        if (truckEquipment.getSerialNumber() != null &&
                truckEquipmentRepository.existsBySerialNumber(truckEquipment.getSerialNumber())) {

            throw new MainServiceException(
                    "Оборудование с серийным номером "
                            + truckEquipment.getSerialNumber()
                            + " уже существует"
            );
        }
        
        if(truckEquipment.getEquipmentTypeId() == null) {
        	throw new MainServiceException(
                    "Не указан тип оборудования "
            );
        }
        
        if(truckEquipment.getTruckId() == null) {
        	throw new MainServiceException(
                    "Не указан TruckId "
            );
        }
        
        Truck truck = truckRepository
                .findById(truckEquipment.getTruckId())
                .orElseThrow(() ->
                        new MainServiceException(
                                "Транспортное средство с ID "
                                        + truckEquipment.getTruckId()
                                        + " не найдено"
                        )
                );
        truckEquipment.setRegistrationNumber(truck.getRegistrationNumber());

        EquipmentType equipmentType =
                equipmentTypeService.getById(truckEquipment.getEquipmentTypeId());

        truckEquipment.setEquipmentTypeName(equipmentType.getName());

        truckEquipment.setCreatedAt(LocalDateTime.now());
        truckEquipment.setStatus(TruckEquipmentStatus.ACTIVE);
        
        // TODO дальше добавить подтягивание mileageStartId и  mileageStartValue и 
      //TODO добавить юзера и фиксацию объекта пробега

        return truckEquipmentRepository.save(truckEquipment);
    }

    @Transactional
    public TruckEquipment update(TruckEquipment truckEquipment) {

        if (truckEquipment.getId() == null) {
            throw new MainServiceException("Отсутствует id в запросе");
        }

        TruckEquipment dbEquipment = truckEquipmentRepository
                .findById(truckEquipment.getId())
                .orElseThrow(() ->
                        new MainServiceException("Оборудование не найдено")
                );

        /*
         * Проверка инвентарного номера.
         * Текущая запись исключается из проверки.
         */
        if (truckEquipment.getInventoryNumber() != null
                && truckEquipmentRepository.existsByInventoryNumberAndIdNot(
                        truckEquipment.getInventoryNumber(),
                        truckEquipment.getId()
                )) {

            throw new MainServiceException(
                    "Оборудование с инвентарным номером "
                            + truckEquipment.getInventoryNumber()
                            + " уже существует"
            );
        }

        /*
         * Проверка серийного номера.
         * Текущая запись исключается из проверки.
         */
        if (truckEquipment.getSerialNumber() != null
                && truckEquipmentRepository.existsBySerialNumberAndIdNot(
                        truckEquipment.getSerialNumber(),
                        truckEquipment.getId()
                )) {

            throw new MainServiceException(
                    "Оборудование с серийным номером "
                            + truckEquipment.getSerialNumber()
                            + " уже существует"
            );
        }

        /*
         * Если меняется машина, проверяем её существование
         * и автоматически обновляем госномер.
         */
        if (truckEquipment.getTruckId() != null) {

            Truck truck = truckRepository
                    .findById(truckEquipment.getTruckId())
                    .orElseThrow(() ->
                            new MainServiceException(
                                    "Транспортное средство с ID "
                                            + truckEquipment.getTruckId()
                                            + " не найдено"
                            )
                    );

            dbEquipment.setTruckId(truck.getId());
            dbEquipment.setRegistrationNumber(
                    truck.getRegistrationNumber()
            );
        }

        /*
         * Если меняется тип оборудования,
         * автоматически обновляем его название.
         */
        if (truckEquipment.getEquipmentTypeId() != null) {

            EquipmentType equipmentType =
                    equipmentTypeService.getById(
                            truckEquipment.getEquipmentTypeId()
                    );

            dbEquipment.setEquipmentTypeId(
                    equipmentType.getId()
            );

            dbEquipment.setEquipmentTypeName(
                    equipmentType.getName()
            );
        }

        dbEquipment.setInventoryNumber(
                truckEquipment.getInventoryNumber() != null
                        ? truckEquipment.getInventoryNumber()
                        : dbEquipment.getInventoryNumber()
        );

        dbEquipment.setBrand(
                truckEquipment.getBrand() != null
                        ? truckEquipment.getBrand()
                        : dbEquipment.getBrand()
        );

        dbEquipment.setModel(
                truckEquipment.getModel() != null
                        ? truckEquipment.getModel()
                        : dbEquipment.getModel()
        );

        dbEquipment.setSerialNumber(
                truckEquipment.getSerialNumber() != null
                        ? truckEquipment.getSerialNumber()
                        : dbEquipment.getSerialNumber()
        );

        dbEquipment.setInstallationDate(
                truckEquipment.getInstallationDate() != null
                        ? truckEquipment.getInstallationDate()
                        : dbEquipment.getInstallationDate()
        );

        dbEquipment.setRemovalDate(
                truckEquipment.getRemovalDate() != null
                        ? truckEquipment.getRemovalDate()
                        : dbEquipment.getRemovalDate()
        );

        dbEquipment.setWorkingHours(
                truckEquipment.getWorkingHours() != null
                        ? truckEquipment.getWorkingHours()
                        : dbEquipment.getWorkingHours()
        );

        dbEquipment.setMileageStartId(
                truckEquipment.getMileageStartId() != null
                        ? truckEquipment.getMileageStartId()
                        : dbEquipment.getMileageStartId()
        );

        dbEquipment.setMileageStartValue(
                truckEquipment.getMileageStartValue() != null
                        ? truckEquipment.getMileageStartValue()
                        : dbEquipment.getMileageStartValue()
        );

        dbEquipment.setMileageEndId(
                truckEquipment.getMileageEndId() != null
                        ? truckEquipment.getMileageEndId()
                        : dbEquipment.getMileageEndId()
        );

        dbEquipment.setMileageEndValue(
                truckEquipment.getMileageEndValue() != null
                        ? truckEquipment.getMileageEndValue()
                        : dbEquipment.getMileageEndValue()
        );

        dbEquipment.setStatus(
                truckEquipment.getStatus() != null
                        ? truckEquipment.getStatus()
                        : dbEquipment.getStatus()
        );

        dbEquipment.setComment(
                truckEquipment.getComment() != null
                        ? truckEquipment.getComment()
                        : dbEquipment.getComment()
        );

        dbEquipment.setUpdatedAt(LocalDateTime.now());

        return truckEquipmentRepository.save(dbEquipment);
    }
}