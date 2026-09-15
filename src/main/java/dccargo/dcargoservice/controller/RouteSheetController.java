package dccargo.dcargoservice.controller;

import dccargo.dcargoservice.dto.dcargo.CompleteRouteSheetDayRequest;
import dccargo.dcargoservice.dto.dcargo.RouteSheetInfoDTO;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.service.dcargo.RouteSheetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/getRouteSheetByTuckAssigment")
    public ResponseEntity<byte[]> generateVehicleWorkCard(
            @RequestParam Long idTruckUserAssigment) throws IOException {

        Map<String, Object> excelData =
                routeSheetService.generateRouteExcelBy(idTruckUserAssigment);

        byte[] excel = (byte[]) excelData.get("bytes");
        String fileName = "Маршрутный лист №T" + idTruckUserAssigment + ".xlsx";

        String contentDisposition =
                "attachment; filename*=UTF-8''" +
                        URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                                .replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(excel);
    }


    @GetMapping("/getRouteSheetInfoByWorkDates")
    public ResponseEntity<List<RouteSheetInfoDTO>> getRouteSheetInfoByWorkDates(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo) {
        List<RouteSheetInfoDTO> routeSheetInfoDTOList =
                routeSheetService.getRouteSheetInfoByWorkDates(dateFrom, dateTo);
        return ResponseEntity.ok(routeSheetInfoDTOList);
    }

    @GetMapping("/getRouteSheetsAccountingReport")
    public ResponseEntity<byte[]> getRouteSheetsAccountingReport(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo) throws IOException {

        byte[] excel =
                routeSheetService.generateRouteSheetsAccountingReport(dateFrom, dateTo);

        String fileName = "Отчет по маршрутным листам " + dateFrom + " - " + dateTo + ".xlsx";

        String contentDisposition =
                "attachment; filename*=UTF-8''" +
                        URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                                .replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(excel);
    }

    @PostMapping("/updateRouteSheet")
    public ResponseEntity<RouteSheetInfoDTO> updateRouteSheet(@RequestBody RouteSheetInfoDTO dto) {
        RouteSheetInfoDTO updated = routeSheetService.updateRouteSheet(dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/completeRouteSheetDay")
    public ResponseEntity<?> completeRouteSheetDay(
            @RequestBody CompleteRouteSheetDayRequest request) {

        if (request.getIdTruckUserAssignment() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 100,
                            "message", "Не заполнено обязательное поле: idTruckUserAssignment"
                    )
            );
        }

        if (request.getEndOdometerValue() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 100,
                            "message", "Не заполнено обязательное поле: endOdometerValue"
                    )
            );
        }

        if (request.getRefWorkTime() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 100,
                            "message", "Не заполнено обязательное поле: refWorkTime"
                    )
            );
        }

        if (request.getVebastoWorkTime() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 100,
                            "message", "Не заполнено обязательное поле: vebastoWorkTime"
                    )
            );
        }

        Map<String, Object> response =
                routeSheetService.completeRouteSheetDay(
                        request.getIdTruckUserAssignment(),
                        request.getStartOdometerValue(),
                        request.getEndOdometerValue(),
                        request.getRefWorkTime(),
                        request.getVebastoWorkTime(),
                        request.getRefuelings()
                );

        return ResponseEntity.ok(response);
    }



}
