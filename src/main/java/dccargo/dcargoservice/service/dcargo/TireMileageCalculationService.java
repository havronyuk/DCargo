package dccargo.dcargoservice.service.dcargo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dccargo.dcargoservice.enums.MileageObjectType;
import dccargo.dcargoservice.enums.MileageSource;
import dccargo.dcargoservice.enums.TireStatus;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.model.dcargo.TruckMileage;
import dccargo.dcargoservice.model.dcargo.TruckTire;
import dccargo.dcargoservice.repository.dcargo.TruckMileageRepository;
import dccargo.dcargoservice.repository.dcargo.TruckTireRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TireMileageCalculationService {
	
	private final TruckMileageRepository truckMileageRepository;
    private final TruckTireRepository truckTireRepository;
    
    @Transactional
    public void calculate(
    		TruckMileage previousTruckMileage,
    		TruckMileage newTruckMileage,
    		Truck truck
    ) {

        if (previousTruckMileage == null) {
            log.info(
                    "Первый пробег автомобиля. "
                            + "Пробег колёс не пересчитывается: truckId={}",
                    newTruckMileage.getObjectId()
            );

            return;
        }

        if (newTruckMileage.getObjectType()
                != MileageObjectType.TRUCK) {

            throw new MainServiceException(
                    "Пересчёт колёс можно запускать "
                            + "только для пробега автомобиля"
            );
        }

        int mileageDelta =
                newTruckMileage.getMileage()
                        - previousTruckMileage.getMileage();

        if (mileageDelta < 0) {
            throw new MainServiceException(
                    "Дельта пробега автомобиля не может быть отрицательной"
            );
        }

        if (mileageDelta == 0) {
            return;
        }

        /*
         * Пока можно использовать колёса,
         * которые сейчас имеют статус INSTALLED.
         */
        List<TruckTire> tires =
                truckTireRepository
                        .findAllByTruckIdAndStatusOrderByAxleNumberAscPositionAsc(
                                newTruckMileage.getObjectId(),
                                TireStatus.INSTALLED
                        );

        for (TruckTire tire : tires) {
            createTireMileage(
                    tire,
                    newTruckMileage,
                    mileageDelta,
                    truck
            );
        }
    }

    private void createTireMileage(
            TruckTire tire,
            TruckMileage truckMileage,
            int mileageDelta,
            Truck truck
    ) {

        boolean alreadyCreated =
                truckMileageRepository
                        .existsByObjectIdAndObjectTypeAndParentMileageId(
                                tire.getId(),
                                MileageObjectType.TIRE,
                                truckMileage.getId()
                        );

        if (alreadyCreated) {
            log.warn(
                    "Пробег колеса уже рассчитан: "
                            + "tireId={}, truckMileageId={}",
                    tire.getId(),
                    truckMileage.getId()
            );

            return;
        }

        int previousTireMileage =
                truckMileageRepository
                        .findFirstByObjectIdAndObjectTypeOrderByMileageDateDescIdDesc(
                                tire.getId(),
                                MileageObjectType.TIRE
                        )
                        .map(TruckMileage::getMileage)
                        .orElse(0);

        TruckMileage tireMileage = new TruckMileage();

        tireMileage.setObjectId(tire.getId());
        tireMileage.setObjectType(MileageObjectType.TIRE);

        tireMileage.setMileage(
                previousTireMileage + mileageDelta
        );

        tireMileage.setMileageDelta(mileageDelta);
        tireMileage.setParentMileageId(truckMileage.getId());

        tireMileage.setMileageDate(
                truckMileage.getMileageDate()
        );

        tireMileage.setSource(MileageSource.AUTO);

        tireMileage.setComment(
                "Автоматически рассчитано "
                        + "по пробегу автомобиля id="
                        + truckMileage.getObjectId()
        );
        
        /*
         * Госномер клиент не передаёт.
         * Получаем актуальный госномер из Truck.
         */
        tireMileage.setRegistrationNumber(
                truck.getRegistrationNumber()
        );

        /*
         * Если дата фиксации не передана,
         * используем текущее время.
         */
        if (truckMileage.getMileageDate() == null) {
        	tireMileage.setMileageDate(LocalDateTime.now());
        }

        /*
         * Если источник не указан,
         * считаем, что пробег внесён вручную.
         */
        if (truckMileage.getSource() == null) {
        	tireMileage.setSource(MileageSource.MANUAL);
        }

        truckMileageRepository.save(tireMileage);
    }

}
