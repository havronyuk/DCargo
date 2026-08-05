package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.Order;
import dccargo.dcargoservice.model.dcargo.SensorRecord;
import dccargo.dcargoservice.service.dcargo.SensorRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SensorRecordController {

    private final SensorRecordService sensorRecordService;

    @GetMapping("/getSensorRecordById")
    public ResponseEntity<SensorRecord> getSensorRecordById(@RequestParam Long idSensorRecord){
        SensorRecord sensorRecord = sensorRecordService.getSensorRecordById(idSensorRecord);
        return ResponseEntity.ok(sensorRecord);
    }

    @GetMapping("/getSensorRecordByIdOrder")
    public ResponseEntity<List<SensorRecord>> getSensorRecordByIdOrder(@RequestParam Long idOrder){
        List<SensorRecord> sensorRecords = sensorRecordService.getSensorRecordByIdOrder(idOrder);
        return ResponseEntity.ok(sensorRecords);
    }

    @PostMapping("/createSensorRecord")
    public ResponseEntity<SensorRecord> createSensorRecord(@RequestBody SensorRecord sensorRecord) {

        SensorRecord savedRecord = sensorRecordService.createSensorRecord(sensorRecord);

        return ResponseEntity.ok(savedRecord);
    }




}
