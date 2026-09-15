package dccargo.dcargoservice.controller;

import dccargo.dcargoservice.model.dcargo.FuelCard;
import dccargo.dcargoservice.service.dcargo.FuelCardService;
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
public class FuelCardController {

    private final FuelCardService fuelCardService;

    /**
     * Все топливные карты.
     */
    @GetMapping("/getAllFuelCard")
    public ResponseEntity<List<FuelCard>> getAll() {
        return ResponseEntity.ok(fuelCardService.getAll());
    }

    /**
     * Все активные топливные карты.
     */
    @GetMapping("/getAllActiveFuelCard")
    public ResponseEntity<List<FuelCard>> getAllActive() {
        return ResponseEntity.ok(fuelCardService.getAllActive());
    }

    /**
     * Карта по id.
     */
    @GetMapping("/getFuelCard/{id}")
    public ResponseEntity<FuelCard> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fuelCardService.getById(id));
    }

    /**
     * Карта по номеру.
     */
    @GetMapping("/getFuelCardByNumber/{cardNumber}")
    public ResponseEntity<FuelCard> getByCardNumber(@PathVariable String cardNumber) {
        return ResponseEntity.ok(fuelCardService.getByCardNumber(cardNumber));
    }

    /**
     * Создать топливную карту.
     * Если карта с таким номером уже существует - ошибка.
     */
    @PostMapping("/createFuelCard")
    public ResponseEntity<FuelCard> create(@RequestBody FuelCard fuelCard) {
        log.info("Создание топливной карты. Номер: {}", fuelCard.getCardNumber());
        FuelCard savedCard = fuelCardService.create(fuelCard);
        return ResponseEntity.ok(savedCard);
    }

    /**
     * Обновить топливную карту.
     * Обязательно передать id.
     */
    @PostMapping("/updateFuelCard")
    public ResponseEntity<FuelCard> update(@RequestBody FuelCard fuelCard) {
        log.info("Обновление топливной карты. ID: {}", fuelCard.getId());
        FuelCard updatedCard = fuelCardService.update(fuelCard);
        return ResponseEntity.ok(updatedCard);
    }
}