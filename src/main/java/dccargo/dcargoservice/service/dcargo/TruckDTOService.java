package dccargo.dcargoservice.service.dcargo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dccargo.dcargoservice.dto.dcargo.TruckDTO;
import dccargo.dcargoservice.dto.dcargo.mapper.TruckDTOMapper;
import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import dccargo.dcargoservice.enums.TireStatus;
import dccargo.dcargoservice.enums.TruckEquipmentStatus;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.model.dcargo.TruckDocument;
import dccargo.dcargoservice.model.dcargo.TruckEquipment;
import dccargo.dcargoservice.model.dcargo.TruckMileage;
import dccargo.dcargoservice.model.dcargo.TruckTire;
import dccargo.dcargoservice.repository.dcargo.TruckDocumentRepository;
import dccargo.dcargoservice.repository.dcargo.TruckEquipmentRepository;
import dccargo.dcargoservice.repository.dcargo.TruckMileageRepository;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.repository.dcargo.TruckTireRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TruckDTOService {
	
	private final TruckRepository truckRepository;
    private final TruckDocumentRepository truckDocumentRepository;
    private final TruckEquipmentRepository truckEquipmentRepository;
    private final TruckTireRepository truckTireRepository;
    private final TruckMileageRepository truckMileageRepository;
    
    private final TruckDTOMapper truckDTOMapper;
    
    /**
     * Получить одну машину со всеми связанными объектами.
     */
    @Transactional
    public TruckDTO getById(Long truckId) {

        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new MainServiceException(
                        "Автомобиль с ID " + truckId + " не найден"
                ));

        List<TruckDocument> documents =
                truckDocumentRepository
                        .findAllByTruckIdAndStatusOrderByValidUntilDesc(truckId, TechnicalInspectionStatus.ACTIVE);

        List<TruckEquipment> equipment =
                truckEquipmentRepository
                        .findAllByTruckIdAndStatusOrderByInstallationDateDesc(
                                truckId,
                                TruckEquipmentStatus.ACTIVE
                        );

        List<TruckTire> tires =
                truckTireRepository
                        .findAllByTruckIdAndStatusOrderByAxleNumberAscPositionAsc(
                                truckId,
                                TireStatus.INSTALLED
                        );

        List<TruckMileage> mileageHistory =
                truckMileageRepository
                        .findByTruckIdOrderByMileageDateDesc(
                                truckId,
                                PageRequest.of(0, 5)
                        );

        return truckDTOMapper.toDTO(
                truck,
                documents,
                equipment,
                tires,
                mileageHistory
        );
    }

    /**
     * Получить все машины со всеми связанными объектами.
     *
     * Выполняется фиксированное количество запросов:
     * 1 — машины;
     * 1 — документы;
     * 1 — оборудование;
     * 1 — шины;
     * 1 — пробеги.
     */
    @Transactional
    public List<TruckDTO> getAll() {

        List<Truck> trucks = truckRepository.findAll();

        if (trucks.isEmpty()) {
            return List.of();
        }

        List<Long> truckIds = trucks.stream()
                .map(Truck::getId)
                .toList();

        List<TruckDocument> documents =
                truckDocumentRepository.findAllByTruckIdIn(truckIds);

        List<TruckEquipment> equipment =
                truckEquipmentRepository.findAllByTruckIdIn(truckIds);

        List<TruckTire> tires =
                truckTireRepository.findAllByTruckIdIn(truckIds);

        List<TruckMileage> mileageHistory =
                truckMileageRepository.findAllByTruckIdIn(truckIds);

        Map<Long, List<TruckDocument>> documentsByTruck =
                documents.stream()
                        .collect(Collectors.groupingBy(
                                TruckDocument::getTruckId
                        ));

        Map<Long, List<TruckEquipment>> equipmentByTruck =
                equipment.stream()
                        .collect(Collectors.groupingBy(
                                TruckEquipment::getTruckId
                        ));

        Map<Long, List<TruckTire>> tiresByTruck =
                tires.stream()
                        .collect(Collectors.groupingBy(
                                TruckTire::getTruckId
                        ));

        Map<Long, List<TruckMileage>> mileageByTruck =
                mileageHistory.stream()
                        .collect(Collectors.groupingBy(
                                TruckMileage::getTruckId
                        ));

        /*
         * Сортируем дочерние списки уже в Java.
         */
        documentsByTruck.values().forEach(list ->
                list.sort(
                        Comparator.comparing(
                                TruckDocument::getValidUntil,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
        );

        equipmentByTruck.values().forEach(list ->
                list.sort(
                        Comparator.comparing(
                                TruckEquipment::getInstallationDate,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
        );

        tiresByTruck.values().forEach(list ->
                list.sort(
                        Comparator.comparing(
                                TruckTire::getAxleNumber,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        ).thenComparing(
                                TruckTire::getPosition,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                )
        );

        mileageByTruck.values().forEach(list ->
                list.sort(
                        Comparator.comparing(
                                TruckMileage::getMileageDate,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        ).thenComparing(
                                TruckMileage::getId,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
        );

        return trucks.stream()
                .map(truck -> truckDTOMapper.toDTO(
                        truck,

                        documentsByTruck.getOrDefault(
                                truck.getId(),
                                List.of()
                        ),

                        equipmentByTruck.getOrDefault(
                                truck.getId(),
                                List.of()
                        ),

                        tiresByTruck.getOrDefault(
                                truck.getId(),
                                List.of()
                        ),

                        mileageByTruck.getOrDefault(
                                truck.getId(),
                                List.of()
                        )
                ))
                .toList();
    }

}
