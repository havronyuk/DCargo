package dccargo.dcargoservice.repository.dcargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.model.dcargo.FIleCustom;

@Repository
public interface FileCustomRepository extends JpaRepository<FIleCustom, Long> {

}
