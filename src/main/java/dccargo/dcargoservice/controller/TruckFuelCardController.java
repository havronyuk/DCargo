package dccargo.dcargoservice.controller;

import dccargo.dcargoservice.model.dcargo.TruckFuelCard;
import dccargo.dcargoservice.service.dcargo.TruckFuelCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TruckFuelCardController {

    private final TruckFuelCardService truckFuelCardService;

    /**
     * Полная история карт грузовика.
     */
    @GetMapping("/getFuelCardsByTruck/{truckId}")
    public ResponseEntity<List<TruckFuelCard>> getAllByTruckId(
            @PathVariable Long truckId) {

        return ResponseEntity.ok(
                truckFuelCardService.getAllByTruckId(truckId)
        );
    }

    /**
     * Текущая (активная) карта грузовика.
     */
    @GetMapping("/getActiveFuelCardByTruck/{truckId}")
    public ResponseEntity<TruckFuelCard> getActiveByTruckId(
            @PathVariable Long truckId) {

        return ResponseEntity.ok(
                truckFuelCardService.getActiveByTruckId(truckId).orElse(null)
        );
    }

    /**
     * Активная топливная карта грузовика (сама карта, не связка).
     */
    @GetMapping("/getFuelCardByTruck/{truckId}")
    public ResponseEntity<dccargo.dcargoservice.model.dcargo.FuelCard> getFuelCardByTruckId(
            @PathVariable Long truckId) {

        return ResponseEntity.ok(
                truckFuelCardService.getActiveFuelCardByTruckId(truckId)
        );
    }

    /**
     * Привязка по id.
     */
    @GetMapping("/getTruckFuelCard/{id}")
    public ResponseEntity<TruckFuelCard> getById(@PathVariable Long id) {
        return ResponseEntity.ok(truckFuelCardService.getById(id));
    }

    /**
     * Назначить/привязать топливную карту к грузовику.
     * Деактивирует предыдущую активную карту грузовика.
     */
    @PostMapping("/assignFuelCardToTruck")
    public ResponseEntity<TruckFuelCard> create(
            @RequestBody TruckFuelCard truckFuelCard) {

        log.info("Назначение топливной карты. Грузовик: {}, карта: {}",
                truckFuelCard.getIdTruck(),
                truckFuelCard.getIdFuelCard());

        TruckFuelCard saved = truckFuelCardService.create(truckFuelCard);

        return ResponseEntity.ok(saved);
    }

    /**
     * Частичное обновление привязки.
     * Обычно используется для деактивации:
     * передать id записи и isActive=false.
     */
    @PostMapping("/updateTruckFuelCard")
    public ResponseEntity<TruckFuelCard> update(
            @RequestBody TruckFuelCard truckFuelCard) {

        log.info("Обновление привязки. ID: {}, isActive: {}",
                truckFuelCard.getId(),
                truckFuelCard.getIsActive());

        TruckFuelCard updated = truckFuelCardService.update(truckFuelCard);

        return ResponseEntity.ok(updated);
    }
}