package dccargo.dcargoservice.repository.dcargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.model.dcargo.UserDocType;

@Repository
public interface UserDocTypeRepository extends JpaRepository<UserDocType, Long> {

	boolean existsByName(String name);

	boolean existsByCode(String code);

}
