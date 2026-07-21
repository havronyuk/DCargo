package dccargo.dcargoservice.repository.dcargo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.model.dcargo.TruckMileage;

@Repository
public interface TruckMileageRepository extends JpaRepository<TruckMileage, Long> {
	
	/**
     * Получить всю историю пробега автомобиля.
     * Сначала возвращаются самые новые записи.
     */
    List<TruckMileage> findByTruckIdOrderByMileageDateDesc(Long truckId);

    /**
     * Получить последнюю запись пробега автомобиля.
     */
    Optional<TruckMileage> findFirstByTruckIdOrderByMileageDateDescIdDesc(
            Long truckId
    );

    /**
     * Проверка существования точно такой же записи.
     */
    boolean existsByTruckIdAndMileage(
            Long truckId,
            Integer mileage
    );
    
    List<TruckMileage> findByTruckIdOrderByMileageDateDesc(
            Long truckId,
            Pageable pageable
    );

    List<TruckMileage> findAllByTruckIdIn(
            Collection<Long> truckIds
    );

}
