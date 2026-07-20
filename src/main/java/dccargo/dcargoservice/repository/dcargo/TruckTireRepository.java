package dccargo.dcargoservice.repository.dcargo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.enums.TireStatus;
import dccargo.dcargoservice.model.dcargo.TruckTire;

@Repository
public interface TruckTireRepository extends JpaRepository<TruckTire, Long> {

	List<TruckTire> findByTruckId(Long truckId);

    List<TruckTire> findByStatus(TireStatus status);

    boolean existsByInventoryNumber(String inventoryNumber);

    boolean existsByInventoryNumberAndIdNot(
            String inventoryNumber,
            Long id
    );

    boolean existsBySerialNumber(String serialNumber);

    boolean existsBySerialNumberAndIdNot(
            String serialNumber,
            Long id
    );
}
