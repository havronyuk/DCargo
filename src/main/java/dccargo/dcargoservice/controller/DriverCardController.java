package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.DriverCard;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.service.dcargo.DriverCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class DriverCardController {

    private final DriverCardService driverCardService;

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
    public ResponseEntity<?> deactivatePassport(@RequestParam("idDriverCard") Integer idDriverCard){
        Map<String,Object> response = new HashMap<>();
        try {

            response = driverCardService.deactivateDriverCard(idDriverCard);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



}
