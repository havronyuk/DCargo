package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.DriverCard;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.service.dcargo.DriverCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class DriverCardController {

    private final DriverCardService driverCardService;

    @GetMapping("/getDriverCardById")
    public ResponseEntity<DriverCard> getDriverCardById(@RequestParam Long idDriverCard){
        DriverCard driverCard = driverCardService.getDriverCardById(idDriverCard);
        return ResponseEntity.ok(driverCard);
    }

    @GetMapping("/getActualDriverCardByIdUser")
    public ResponseEntity<DriverCard> getActualDriverCardByIdUser(@RequestParam Long idUser){
        DriverCard driverCard = driverCardService.getActualDriverCardByIdUser(idUser);
        return ResponseEntity.ok(driverCard);
    }

    @GetMapping("/getDriverCardsByIdUser")
    public ResponseEntity<List<DriverCard>> getDriverCardsByIdUser(@RequestParam Long idUser){
        List<DriverCard> allCards = driverCardService.getDriverCardsByIdUser(idUser);
        return ResponseEntity.ok(allCards);
    }


    @PostMapping("/createDriverCard")
    public ResponseEntity<DriverCard> create(@RequestBody DriverCard driverCard) {
        DriverCard savedCard = driverCardService.create(driverCard);

        return ResponseEntity.ok(savedCard);
    }

    @PostMapping("/updateDriverCard")
    public ResponseEntity<DriverCard> update(@RequestBody DriverCard driverCard) {

        DriverCard savedCard = driverCardService.update(driverCard);

        return ResponseEntity.ok(savedCard);
    }

    @PostMapping("/deactivateDriverCard")
    public ResponseEntity<?> deactivatePassport(@RequestParam("idDriverCard") Long idDriverCard){
        Map<String,Object> response = new HashMap<>();
        try {

            response = driverCardService.deactivateDriverCard(idDriverCard);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



}
