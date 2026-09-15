package dccargo.dcargoservice.service.dcargo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import dccargo.dcargoservice.model.dcargo.*;
import dccargo.dcargoservice.repository.dcargo.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dccargo.dcargoservice.dto.dcargo.TruckDTO;
import dccargo.dcargoservice.dto.dcargo.mapper.TruckDTOMapper;
import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import dccargo.dcargoservice.enums.TireStatus;
import dccargo.dcargoservice.enums.TruckEquipmentStatus;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TruckDTOService {
	
	private final TruckRepository truckRepository;
    private final TruckDocumentRepository truckDocumentRepository;
    private final TruckEquipmentRepository truckEquipmentRepository;
    private final TruckTireRepository truckTireRepository;
    private final TruckMileageRepository truckMileageRepository;
    private final FuelCardRepository fuelCardRepository;
    private final TruckFuelCardRepository truckFuelCardRepository;

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
                        .findByObjectIdOrderByMileageDateDesc(
                                truckId,
                                PageRequest.of(0, 5)
                        );

        FuelCard fuelCard = truckFuelCardRepository
                .findFirstByIdTruckAndIsActiveTrueOrderByCreatedAtDesc(truckId)
                .flatMap(link -> fuelCardRepository.findById(link.getIdFuelCard()))
                .orElse(null);

        return truckDTOMapper.toDTO(
                truck,
                documents,
                equipment,
                tires,
                mileageHistory,
                fuelCard
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
                truckMileageRepository.findAllByObjectIdIn(truckIds);

        /*
         * Активные топливные карты грузовиков.
         * 1 запрос — активные связки truck_fuel_card;
         * 1 запрос — сами карты fuel_card.
         */
        List<TruckFuelCard> activeLinks =
                truckFuelCardRepository.findAllByIdTruckInAndIsActiveTrue(truckIds);

        List<Long> fuelCardIds = activeLinks.stream()
                .map(TruckFuelCard::getIdFuelCard)
                .distinct()
                .toList();

        Map<Long, FuelCard> fuelCardsById = fuelCardIds.isEmpty()
                ? Map.of()
                : fuelCardRepository.findAllById(fuelCardIds).stream()
                        .collect(Collectors.toMap(
                                FuelCard::getId,
                                card -> card
                        ));

        Map<Long, FuelCard> fuelCardByTruck = activeLinks.stream()
                .collect(Collectors.toMap(
                        TruckFuelCard::getIdTruck,
                        link -> fuelCardsById.get(link.getIdFuelCard())
                ));

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
                                TruckMileage::getObjectId
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
                        ),

                        fuelCardByTruck.get(truck.getId())
                ))
                .toList();
    }

}
