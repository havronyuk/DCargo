package dccargo.dcargoservice.repository.dcargo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.enums.MileageObjectType;
import dccargo.dcargoservice.model.dcargo.TruckMileage;

@Repository
public interface TruckMileageRepository extends JpaRepository<TruckMileage, Long> {
	
	/**
     * Получить всю историю пробега автомобиля.
     * Сначала возвращаются самые новые записи.
     */
    List<TruckMileage> findByObjectIdOrderByMileageDateDesc(Long truckId);

    /**
     * Получить последнюю запись пробега автомобиля.
     */
    Optional<TruckMileage> findFirstByObjectIdOrderByMileageDateDescIdDesc(
            Long truckId
    );

    /**
     * Проверка существования точно такой же записи.
     */
    boolean existsByObjectIdAndMileage(
            Long truckId,
            Integer mileage
    );
    
    List<TruckMileage> findByObjectIdOrderByMileageDateDesc(
            Long truckId,
            Pageable pageable
    );

    List<TruckMileage> findAllByObjectIdIn(
            Collection<Long> truckIds
    );
    
    /**
     * Проверяет, была ли уже создана запись пробега объекта
     * на основании указанной родительской записи пробега.
     *
     * Например: был ли уже начислен пробег конкретному колесу
     * по конкретной записи пробега автомобиля.
     */
    boolean existsByObjectIdAndObjectTypeAndParentMileageId(
            Long objectId,
            MileageObjectType objectType,
            Long parentMileageId
    );

    /**
     * Получить последнюю запись пробега конкретного объекта
     * с учётом типа объекта.
     */
    Optional<TruckMileage>
            findFirstByObjectIdAndObjectTypeOrderByMileageDateDescIdDesc(
                    Long objectId,
                    MileageObjectType objectType
            );

}
