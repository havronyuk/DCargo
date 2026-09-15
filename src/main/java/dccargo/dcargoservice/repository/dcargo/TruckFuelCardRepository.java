package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.TruckFuelCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TruckFuelCardRepository extends JpaRepository<TruckFuelCard, Long> {

    /**
     * Полная история карт по грузовику.
     */
    List<TruckFuelCard> findAllByIdTruckOrderByCreatedAtDesc(Long idTruck);

    /**
     * Текущая (активная) карта грузовика.
     */
    Optional<TruckFuelCard> findFirstByIdTruckAndIsActiveTrueOrderByCreatedAtDesc(Long idTruck);

    /**
     * Массовая загрузка активных карт для списка грузовиков.
     */
    List<TruckFuelCard> findAllByIdTruckInAndIsActiveTrue(Collection<Long> idTrucks);

    /**
     * Все связки с картой (для истории использования карты).
     */
    List<TruckFuelCard> findByIdFuelCard(Long idFuelCard);

    boolean existsByIdTruckAndIdFuelCard(Long idTruck, Long idFuelCard);

}