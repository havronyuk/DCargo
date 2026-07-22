package dccargo.dcargoservice.repository.dcargo;


import dccargo.dcargoservice.model.dcargo.DriverCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverCardRepository  extends JpaRepository<DriverCard, Long> {

    Optional<DriverCard> findByIdDriverCard(Long idDriverCard);

    Optional<DriverCard> findByIdUserAndBlock(Long idUser, Boolean block);

    boolean existsByNumber(String number);

    boolean existsByIdUserAndBlock(Long idUser, Boolean block);

    List<DriverCard> findAllByIdUser(Long idUser);

}
