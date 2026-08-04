package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.SensorRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRecordRepository extends JpaRepository<SensorRecord,Long> {

    SensorRecord findByIdSensorRecord(Long idSensorRecord);

    List<SensorRecord> findAllByIdOrder(Long idOrder);

}
