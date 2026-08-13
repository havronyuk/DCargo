package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByIdUser(Long idUser);

	List<User> findAllByBlockIsFalse();

	List<User> findAllByIdUserIn(List<Long> idUsers);

    boolean existsByLoginTelephoneAndBlockIsFalse(String loginTelephone);

    boolean existsByIdUser(Long idUser);

    boolean existsByLoginAndBlockIsFalse(String login);

    boolean existsByEmailAndAndBlockIsFalse(String email);

}
