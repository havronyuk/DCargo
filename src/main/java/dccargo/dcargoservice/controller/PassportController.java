package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.service.dcargo.PassportService;
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
public class PassportController {

    private final PassportService passportService;

    @PostMapping("/createPassport")
    public ResponseEntity<Passport> create(@RequestBody Passport passport) {
        Passport savedPassport = passportService.create(passport);

        return ResponseEntity.ok(savedPassport);
    }

    @PostMapping("/updatePassport")
    public ResponseEntity<Passport> update(@RequestBody Passport passport) {

        Passport savedPassport = passportService.update(passport);

        return ResponseEntity.ok(savedPassport);
    }

    @PostMapping("/deactivatePassport")
    public ResponseEntity<?> deactivatePassport(@RequestParam("idPassport") Integer idPassport){
        Map<String,Object> response = new HashMap<>();
        try {

            response = passportService.deactivatePassport(idPassport);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



}
