package dccargo.dcargoservice.repository.dcargo;


import dccargo.dcargoservice.enums.TruckStatus;
import dccargo.dcargoservice.model.dcargo.Truck;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {
	
	Optional<Truck> findByRegistrationNumber(String registrationNumber);

    Optional<Truck> findByVin(String vin);

    Optional<Truck> findByInternalNumber(String internalNumber);

    Optional<Truck> findByGarageNumber(String garageNumber);

    List<Truck> findByStatus(TruckStatus status);

    boolean existsByRegistrationNumber(String registrationNumber);

    boolean existsByVin(String vin);
}
