package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.enums.FuelCardStatus;
import dccargo.dcargoservice.model.dcargo.FuelCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuelCardRepository extends JpaRepository<FuelCard, Long> {

    Optional<FuelCard> findById(Long id);

    Optional<FuelCard> findByCardNumber(String cardNumber);

    List<FuelCard> findByStatus(FuelCardStatus status);

    boolean existsByCardNumber(String cardNumber);

}