package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByIdUser(Long idUser);

    boolean existsByLoginTelephoneAndBlockIsFalse(String loginTelephone);

    boolean existsByIdUser(Long idUser);



}
