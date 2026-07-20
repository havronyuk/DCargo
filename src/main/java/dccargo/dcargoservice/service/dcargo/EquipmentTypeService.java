package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import dccargo.dcargoservice.model.dcargo.EquipmentType;
import dccargo.dcargoservice.repository.dcargo.EquipmentTypeRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EquipmentTypeService {

    private final EquipmentTypeRepository equipmentTypeRepository;

    /**
     * Получить все типы оборудования.
     */
    public List<EquipmentType> getAll() {
        return equipmentTypeRepository.findAll();
    }

    /**
     * Создать новый тип оборудования.
     */
    public EquipmentType create(EquipmentType equipmentType) {

        if (equipmentTypeRepository.existsByName(equipmentType.getName())) {
            throw new MainServiceException(
                    "Тип оборудования с названием \""
                            + equipmentType.getName()
                            + "\" уже существует"
            );
        }

        if (equipmentTypeRepository.existsByCode(equipmentType.getCode())) {
            throw new MainServiceException(
                    "Тип оборудования с кодом \""
                            + equipmentType.getCode()
                            + "\" уже существует"
            );
        }

        equipmentType.setCreatedAt(LocalDateTime.now());
        equipmentType.setActive(true);

        // TODO: После внедрения авторизации
        // equipmentType.setCreatedByUserId(SecurityUtils.getCurrentUserId());

        return equipmentTypeRepository.save(equipmentType);
    }

    /**
     * Получить тип оборудования по ID.
     */
    public EquipmentType getById(Long id) {

        return equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new MainServiceException(
                        "Тип оборудования не найден"
                ));
    }
    
    public EquipmentType getByCode(String code) {
        return equipmentTypeRepository.findByCode(code)
                .orElseThrow(() -> new MainServiceException(
                        "Тип оборудования с кодом \"" + code + "\" не найден"
                ));
    }

}