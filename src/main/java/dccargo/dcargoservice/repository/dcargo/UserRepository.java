package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.User;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByIdUser(Integer idUser);

    boolean existsByLoginTelephoneAndBlockIsFalse(String loginTelephone);

    boolean existsByIdUser(Integer idUser);



}
