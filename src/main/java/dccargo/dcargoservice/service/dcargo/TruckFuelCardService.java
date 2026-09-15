package dccargo.dcargoservice.service.dcargo;

import dccargo.dcargoservice.audit.Audited;
import dccargo.dcargoservice.model.dcargo.TruckFuelCard;
import dccargo.dcargoservice.repository.dcargo.TruckFuelCardRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import dccargo.dcargoservice.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import dccargo.dcargoservice.model.dcargo.FuelCard;
import dccargo.dcargoservice.repository.dcargo.FuelCardRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TruckFuelCardService {

    private final TruckFuelCardRepository truckFuelCardRepository;

    private final FuelCardRepository fuelCardRepository;

    private final SecurityUtils securityUtils;

    /**
     * История всех карт по грузовику.
     */
    public List<TruckFuelCard> getAllByTruckId(Long truckId) {
        if (truckId == null) {
            throw new MainServiceException("Не указан id грузовика");
        }
        return truckFuelCardRepository.findAllByIdTruckOrderByCreatedAtDesc(truckId);
    }

    /**
     * Текущая (активная) карта грузовика.
     * Может вернуть null, если карта не назначена.
     */
    public Optional<TruckFuelCard> getActiveByTruckId(Long truckId) {
        if (truckId == null) {
            throw new MainServiceException("Не указан id грузовика");
        }
        return truckFuelCardRepository.findFirstByIdTruckAndIsActiveTrueOrderByCreatedAtDesc(truckId);
    }

    /**
     * Активная топливная карта грузовика.
     * Может вернуть null, если карта не назначена.
     */
    public FuelCard getActiveFuelCardByTruckId(Long truckId) {
        return getActiveByTruckId(truckId)
                .map(link -> fuelCardRepository.findById(link.getIdFuelCard())
                        .orElseThrow(() -> new MainServiceException(
                                "Топливная карта не найдена"
                        )))
                .orElse(null);
    }

    public TruckFuelCard getById(Long id) {
        if (id == null) {
            throw new MainServiceException("Не указан id связи карта-грузовик");
        }
        return truckFuelCardRepository.findById(id)
                .orElseThrow(() -> new MainServiceException(
                        "Запись привязки топливной карты к грузовику не найдена"
                ));
    }

    /**
     * Назначить топливную карту на грузовик.
     * - Проверяет, что связки ещё нет.
     * - Деактивирует текущую активную карту (если есть).
     * - Создаёт новую запись как active.
     */
    @Audited(operation = "ASSIGN_FUEL_CARD_TO_TRUCK")
    @Transactional
    public TruckFuelCard create(TruckFuelCard truckFuelCard) {

        if (truckFuelCard.getIdTruck() == null) {
            throw new MainServiceException("Не указан id грузовика");
        }
        if (truckFuelCard.getIdFuelCard() == null) {
            throw new MainServiceException("Не указан id топливной карты");
        }

        if (truckFuelCardRepository.existsByIdTruckAndIdFuelCard(
                truckFuelCard.getIdTruck(),
                truckFuelCard.getIdFuelCard())) {
            throw new MainServiceException(
                    "Данная топливная карта уже привязана к этому грузовику"
            );
        }

        // деактивируем текущую активную карту
        truckFuelCardRepository
                .findFirstByIdTruckAndIsActiveTrueOrderByCreatedAtDesc(
                        truckFuelCard.getIdTruck())
                .ifPresent(current -> {
                    current.setIsActive(false);
                    truckFuelCardRepository.save(current);
                });

        // создаём новую запись
        truckFuelCard.setIsActive(true);

        truckFuelCard.setCreatedByUserId(securityUtils.getCurrentUserId());
        truckFuelCard.setCreatedByUserName(securityUtils.getCurrentUsername());

        return truckFuelCardRepository.save(truckFuelCard);
    }

    /**
     * Частичное обновление привязки.
     * Обычно используется для деактивации (isActive = false).
     * Обязательно передать id.
     */
    @Audited(operation = "UPDATE_TRUCK_FUEL_CARD")
    @Transactional
    public TruckFuelCard update(TruckFuelCard truckFuelCard) {

        if (truckFuelCard.getId() == null) {
            throw new MainServiceException("Отсутствует id в запросе");
        }

        TruckFuelCard dbRecord = truckFuelCardRepository.findById(truckFuelCard.getId())
                .orElseThrow(() -> new MainServiceException(
                        "Запись привязки топливной карты к грузовику не найдена"
                ));

        if (truckFuelCard.getIsActive() != null) {
            dbRecord.setIsActive(truckFuelCard.getIsActive());
        }

        if (truckFuelCard.getComment() != null) {
            dbRecord.setComment(truckFuelCard.getComment());
        }

        return truckFuelCardRepository.save(dbRecord);
    }
}
