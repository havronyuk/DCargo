package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Integer> {

    boolean existsByPersonalNumber(String personalNumber);

    Passport findByIdPassport(Integer idPassport);

}
