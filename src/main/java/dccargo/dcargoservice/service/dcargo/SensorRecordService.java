package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.model.dcargo.SensorRecord;
import dccargo.dcargoservice.repository.dcargo.SensorRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SensorRecordService {

    private final SensorRecordRepository sensorRecordRepository;


    public SensorRecord getSensorRecordById(Long idSensorRecord) {
        return sensorRecordRepository.findByIdSensorRecord(idSensorRecord);

    }

    public List<SensorRecord> getSensorRecordByIdOrder(Long idOrder) {
        return sensorRecordRepository.findAllByIdOrder(idOrder);
    }


    public SensorRecord createSensorRecord(SensorRecord sensorRecord) {
        return null;
    }



}
