package dccargo.dcargoservice.repository.dcargo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dccargo.dcargoservice.model.dcargo.EquipmentType;

public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Long> {

    boolean existsByName(String name);

    boolean existsByCode(String code);
    
    Optional<EquipmentType> findByCode(String code);

}