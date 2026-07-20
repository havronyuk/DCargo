package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Integer> {

    boolean existsByPersonalNumber(String personalNumber);

    boolean existsByIdUserAndBlock(Integer idUser,Boolean block);

    Passport findByIdUserAndBlock(Integer idUser,Boolean block);

    Passport findByIdPassport(Integer idPassport);

    List<Passport> findAllByIdUser(Integer idUser);

}
