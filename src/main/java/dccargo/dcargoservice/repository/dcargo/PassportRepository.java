package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {

    List<Passport> findAllByPersonalNumber(String personalNumber);

    boolean existsByIdUserAndBlock(Long idUser,Boolean block);

    Passport findByIdUserAndBlock(Long idUser,Boolean block);

    Passport findByIdPassport(Long idPassport);

    List<Passport> findAllByIdUser(Long idUser);

    List<Passport> findAllByIdUserIn(Collection<Long> userIds);

}
