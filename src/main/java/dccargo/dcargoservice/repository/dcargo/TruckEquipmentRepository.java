package dccargo.dcargoservice.repository.dcargo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.enums.TruckEquipmentStatus;
import dccargo.dcargoservice.model.dcargo.TruckEquipment;

@Repository
public interface TruckEquipmentRepository extends JpaRepository<TruckEquipment, Long> {

    List<TruckEquipment> findByTruckId(Long truckId);

    List<TruckEquipment> findByEquipmentTypeId(Long equipmentTypeId);

    List<TruckEquipment> findByStatus(TruckEquipmentStatus status);

    boolean existsByInventoryNumber(String inventoryNumber);

    boolean existsBySerialNumber(String serialNumber);
    
    boolean existsByInventoryNumberAndIdNot(
            String inventoryNumber,
            Long id
    );

    boolean existsBySerialNumberAndIdNot(
            String serialNumber,
            Long id
    );
}