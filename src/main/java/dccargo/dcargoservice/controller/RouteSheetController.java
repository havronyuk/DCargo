package dccargo.dcargoservice.controller;

import dccargo.dcargoservice.service.dcargo.RouteSheetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class RouteSheetController {

    private final RouteSheetService routeSheetService;

//    @GetMapping("/loadRootExcel")
//    public ResponseEntity<String> generateVehicleWorkCard() {
//
//        log.info("🔥 GET /loadRootExcel");
//
//        return ResponseEntity.ok("OK");
//    }


    @GetMapping("/loadRootExcel")
    public ResponseEntity<byte[]> generateVehicleWorkCard() throws IOException {

        byte[] excel = routeSheetService.generateEmptyExcel();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"vehicle_work_card.xlsx\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(excel);
    }



}
