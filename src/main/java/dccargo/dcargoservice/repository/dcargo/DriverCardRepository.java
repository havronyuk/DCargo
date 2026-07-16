package dccargo.dcargoservice.repository.dcargo;


import dccargo.dcargoservice.model.dcargo.DriverCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverCardRepository  extends JpaRepository<DriverCard, Integer> {

    DriverCard findByIdDriverCard(Integer idDriverCard);

    boolean existsByNumber(String number);


}
