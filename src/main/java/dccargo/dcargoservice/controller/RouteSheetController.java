package dccargo.dcargoservice.controller;

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
            @RequestBody Map<String, Object> request) {

        String[] requiredFields = {
                "idTruckUserAssignment",
                "startOdometerValue",
                "endOdometerValue",
                "refWorkTime",
                "vebastoWorkTime",
                "fuelAmount"
        };

        for (String field : requiredFields) {
            if (!request.containsKey(field) || request.get(field) == null) {
                return ResponseEntity.badRequest().body(
                        Map.of(
                                "status", 100,
                                "message", "Не заполнено обязательное поле: " + field
                        )
                );
            }
        }

        if (!(request.get("idTruckUserAssignment") instanceof Number)) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", 100,
                            "message", "Поле idTruckUserAssignment должно быть числом")
            );
        }

        if (!(request.get("startOdometerValue") instanceof Number)) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", 100,
                            "message", "Поле startOdometerValue должно быть числом")
            );
        }

        if (!(request.get("endOdometerValue") instanceof Number)) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", 100,
                            "message", "Поле endOdometerValue должно быть числом")
            );
        }

        if (!(request.get("refWorkTime") instanceof Number)) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", 100,
                            "message", "Поле refWorkTime должно быть числом")
            );
        }

        if (!(request.get("vebastoWorkTime") instanceof Number)) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", 100,
                            "message", "Поле vebastoWorkTime должно быть числом")
            );
        }

        if (!(request.get("fuelAmount") instanceof Number)) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", 100,
                            "message", "Поле fuelAmount должно быть числом")
            );
        }

        Long idTruckUserAssignment =
                ((Number) request.get("idTruckUserAssignment")).longValue();

        Integer startOdometerValue =
                ((Number) request.get("startOdometerValue")).intValue();

        Integer endOdometerValue =
                ((Number) request.get("endOdometerValue")).intValue();

        Double refWorkTime =
                ((Number) request.get("refWorkTime")).doubleValue();

        Double vebastoWorkTime =
                ((Number) request.get("vebastoWorkTime")).doubleValue();

        Double fuelAmount =
                ((Number) request.get("fuelAmount")).doubleValue();

        Map<String, Object> response =
                routeSheetService.completeRouteSheetDay(
                        idTruckUserAssignment,
                        startOdometerValue,
                        endOdometerValue,
                        refWorkTime,
                        vebastoWorkTime,
                        fuelAmount
                );

        return ResponseEntity.ok(response);
    }



}
