package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dccargo.dcargoservice.enums.MileageSource;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.model.dcargo.TruckMileage;
import dccargo.dcargoservice.repository.dcargo.TruckMileageRepository;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TruckMileageService {
	
	private final TruckMileageRepository truckMileageRepository;

    private final TruckRepository truckRepository;
    
    /**
     * Получить все записи пробега.
     */
    public List<TruckMileage> getAllTruckMileage() {
        return truckMileageRepository.findAll();
    }

    /**
     * Получить историю пробега конкретного автомобиля.
     */
    public List<TruckMileage> getByTruckId(Long truckId) {

        if (truckId == null) {
            throw new MainServiceException("Не указан TruckId");
        }

        if (!truckRepository.existsById(truckId)) {
            throw new MainServiceException(
                    "Транспортное средство с id "
                            + truckId
                            + " не найдено"
            );
        }

        return truckMileageRepository
                .findByTruckIdOrderByMileageDateDesc(truckId);
    }

    /**
     * Получить последнюю запись пробега автомобиля.
     */
    public TruckMileage getLastMileage(Long truckId) {

        if (truckId == null) {
            throw new MainServiceException("Не указан TruckId");
        }

        if (!truckRepository.existsById(truckId)) {
            throw new MainServiceException(
                    "Транспортное средство с id "
                            + truckId
                            + " не найдено"
            );
        }

        return truckMileageRepository
                .findFirstByTruckIdOrderByMileageDateDescIdDesc(truckId)
                .orElseThrow(() -> new MainServiceException(
                        "Для транспортного средства с id "
                                + truckId
                                + " пробег ещё не зафиксирован"
                ));
    }

    /**
     * Создать новую запись пробега.
     */
    @Transactional
    public TruckMileage create(TruckMileage truckMileage) {

        if (truckMileage == null) {
            throw new MainServiceException(
                    "Не переданы данные пробега"
            );
        }

        if (truckMileage.getTruckId() == null) {
            throw new MainServiceException(
                    "Не указан TruckId"
            );
        }

        if (truckMileage.getMileage() == null) {
            throw new MainServiceException(
                    "Не указан пробег автомобиля"
            );
        }

        if (truckMileage.getMileage() < 0) {
            throw new MainServiceException(
                    "Пробег автомобиля не может быть отрицательным"
            );
        }

        Truck truck = truckRepository
                .findById(truckMileage.getTruckId())
                .orElseThrow(() -> new MainServiceException(
                        "Транспортное средство с id "
                                + truckMileage.getTruckId()
                                + " не найдено"
                ));

        /*
         * Получаем последний известный пробег автомобиля
         * и проверяем, что новый пробег не меньше предыдущего.
         */
        truckMileageRepository
                .findFirstByTruckIdOrderByMileageDateDescIdDesc(
                        truckMileage.getTruckId()
                )
                .ifPresent(lastMileage -> {

                    if (truckMileage.getMileage()
                            < lastMileage.getMileage()) {

                        throw new MainServiceException(
                                "Новый пробег "
                                        + truckMileage.getMileage()
                                        + " км меньше последнего пробега "
                                        + lastMileage.getMileage()
                                        + " км"
                        );
                    }
                });

        /*
         * Не создаём полную копию записи
         * с таким же TruckId и значением пробега.
         */
        if (truckMileageRepository.existsByTruckIdAndMileage(
                truckMileage.getTruckId(),
                truckMileage.getMileage())) {

            throw new MainServiceException(
                    "Пробег "
                            + truckMileage.getMileage()
                            + " км уже зарегистрирован для этого автомобиля"
            );
        }

        /*
         * Госномер клиент не передаёт.
         * Получаем актуальный госномер из Truck.
         */
        truckMileage.setRegistrationNumber(
                truck.getRegistrationNumber()
        );

        /*
         * Если дата фиксации не передана,
         * используем текущее время.
         */
        if (truckMileage.getMileageDate() == null) {
            truckMileage.setMileageDate(LocalDateTime.now());
        }

        /*
         * Если источник не указан,
         * считаем, что пробег внесён вручную.
         */
        if (truckMileage.getSource() == null) {
            truckMileage.setSource(MileageSource.MANUAL);
        }

        /*
         * ID и дата создания должны назначаться
         * базой данных, а не клиентом.
         */
        truckMileage.setId(null);
        truckMileage.setCreatedAt(null);

        TruckMileage savedMileage =
                truckMileageRepository.save(truckMileage);

        log.info(
                "Создана запись пробега: truckId={}, mileage={}, source={}",
                savedMileage.getTruckId(),
                savedMileage.getMileage(),
                savedMileage.getSource()
        );

        return savedMileage;
    }

}
