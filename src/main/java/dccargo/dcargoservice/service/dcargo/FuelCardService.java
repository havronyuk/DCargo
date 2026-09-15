package dccargo.dcargoservice.service.dcargo;

import dccargo.dcargoservice.audit.Audited;
import dccargo.dcargoservice.enums.FuelCardStatus;
import dccargo.dcargoservice.model.dcargo.FuelCard;
import dccargo.dcargoservice.repository.dcargo.FuelCardRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import dccargo.dcargoservice.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FuelCardService {

    private final FuelCardRepository fuelCardRepository;

    private final SecurityUtils securityUtils;

    public List<FuelCard> getAll() {
        return fuelCardRepository.findAll();
    }

    public FuelCard getById(Long id) {
        if (id == null) {
            throw new MainServiceException("Не указан id топливной карты");
        }
        return fuelCardRepository.findById(id)
                .orElseThrow(() -> new MainServiceException(
                        "Топливная карта не найдена"
                ));
    }

    public FuelCard getByCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new MainServiceException("Не указан номер топливной карты");
        }
        return fuelCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new MainServiceException(
                        "Топливная карта с номером \"" + cardNumber + "\" не найдена"
                ));
    }

    public List<FuelCard> getAllActive() {
        return fuelCardRepository.findByStatus(FuelCardStatus.ACTIVE);
    }

    @Transactional
    public FuelCard create(FuelCard fuelCard) {

        if (fuelCard.getCardNumber() == null || fuelCard.getCardNumber().isBlank()) {
            throw new MainServiceException("Не указан номер топливной карты");
        }

        if (fuelCardRepository.existsByCardNumber(fuelCard.getCardNumber())) {
            throw new MainServiceException(
                    "Топливная карта с номером \""
                            + fuelCard.getCardNumber()
                            + "\" уже существует"
            );
        }

        if (fuelCard.getStatus() == null) {
            fuelCard.setStatus(FuelCardStatus.ACTIVE);
        }

        fuelCard.setCreatedByUserId(securityUtils.getCurrentUserId());
        fuelCard.setCreatedByUserName(securityUtils.getCurrentUsername());

        return fuelCardRepository.save(fuelCard);
    }

    @Audited(operation = "UPDATE_FUEL_CARD")
    @Transactional
    public FuelCard update(FuelCard fuelCard) {

        if (fuelCard.getId() == null) {
            throw new MainServiceException("Отсутствует id в запросе");
        }

        FuelCard dbCard = fuelCardRepository.findById(fuelCard.getId())
                .orElseThrow(() -> new MainServiceException(
                        "Топливная карта не найдена"
                ));

        if (fuelCard.getCardNumber() != null) {
            if (!fuelCard.getCardNumber().equals(dbCard.getCardNumber())
                    && fuelCardRepository.existsByCardNumber(fuelCard.getCardNumber())) {
                throw new MainServiceException(
                        "Топливная карта с номером \""
                                + fuelCard.getCardNumber()
                                + "\" уже существует"
                );
            }
            dbCard.setCardNumber(fuelCard.getCardNumber());
        }

        if (fuelCard.getFuelType() != null) {
            dbCard.setFuelType(fuelCard.getFuelType());
        }

        if (fuelCard.getStatus() != null) {
            dbCard.setStatus(fuelCard.getStatus());
        }

        if (fuelCard.getComment() != null) {
            dbCard.setComment(fuelCard.getComment());
        }

        return fuelCardRepository.save(dbCard);
    }
}
