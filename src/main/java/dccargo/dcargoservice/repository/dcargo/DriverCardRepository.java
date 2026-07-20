package dccargo.dcargoservice.repository.dcargo;


import dccargo.dcargoservice.model.dcargo.DriverCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverCardRepository  extends JpaRepository<DriverCard, Integer> {

    DriverCard findByIdDriverCard(Integer idDriverCard);

    DriverCard findByIdUserAndBlock(Integer idUser, Boolean block);

    boolean existsByNumber(String number);

    boolean existsByIdUserAndBlock(Integer idUser, Boolean block);

    List<DriverCard> findAllByIdUser(Integer idUser);

}
