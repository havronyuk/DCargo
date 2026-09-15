package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.audit.Audited;
import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import dccargo.dcargoservice.model.dcargo.DriverCard;
import dccargo.dcargoservice.model.dcargo.UserDocument;
import dccargo.dcargoservice.repository.dcargo.DriverCardRepository;
import dccargo.dcargoservice.repository.dcargo.UserDocumentRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import dccargo.dcargoservice.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverCardService {

    private final DriverCardRepository driverCardRepository;

    private final UserDocumentRepository userDocumentRepository;

    private final SecurityUtils securityUtils;

    // Типы документов, привязанные к водительскому удостоверению
    private static final long DOCUMENT_TYPE_INTERNATIONAL_VU = 4L;
    private static final long DOCUMENT_TYPE_INTERNATIONAL_VU_ALT = 5L;

    public DriverCard create(DriverCard driverCard) {


        if(driverCard.getIdDriverCard() != null){
            throw new MainServiceException("Нельзя создать запись, если ID уже существует");
        }

        if(driverCard.getIdUser() == null){
            throw new MainServiceException("Нельзя создать запись без привязки к пользователю");
        }

        boolean existActual = driverCardRepository.existsByIdUserAndBlock(driverCard.getIdUser(),false);

        if(existActual){
            throw new MainServiceException("Создание новой записи запрещено. Заблокируйте актуальную запись");
        }

        if(driverCard.getNumber() != null){
            driverCard.setNumber(normalize(driverCard.getNumber()));
        }

        if(driverCardRepository.existsByNumber(driverCard.getNumber())){
            throw new MainServiceException("Водительское удостоверение с данным номером уже существует");
        }

        driverCard.setCreatedAt(LocalDateTime.now());
        driverCard.setCreatedByUserId(securityUtils.getCurrentUserId());
        driverCard.setCreatedByUserName(securityUtils.getCurrentUsername());
        driverCard.setFromSystem("Yard");

        return driverCardRepository.save(driverCard);
    }

    /**
     * Метод обновления водительского удостоверения. <br> <b>Важно: проверяет все поля вручную!</b>
     * @param driverCard
     * @return
     */
    @Audited(operation = "UPDATE_DRIVER_CARD")
    @Transactional
    public DriverCard update(DriverCard driverCard) {
        if (driverCard.getIdDriverCard() == null) {
            throw new MainServiceException("Отсутствует id в запросе");
        }

        DriverCard dbDriverCard = driverCardRepository.findByIdDriverCard(driverCard.getIdDriverCard())
                .orElseThrow(() -> new MainServiceException("Водительское удостоверение c ID " + driverCard.getIdDriverCard() + " не обнаружено"));

        if(!dbDriverCard.getIdUser().equals(driverCard.getIdUser())){
            throw new MainServiceException("Нельзя передать удостоверение на другого пользователя");
        }

        dbDriverCard.setNumber(driverCard.getNumber() != null ? normalize(driverCard.getNumber()) : dbDriverCard.getNumber());
        dbDriverCard.setIssueDate(driverCard.getIssueDate() != null ? driverCard.getIssueDate() : dbDriverCard.getIssueDate());
        dbDriverCard.setExpiryDate(driverCard.getExpiryDate() != null ? driverCard.getExpiryDate() : dbDriverCard.getExpiryDate());
        dbDriverCard.setIssuedBy(driverCard.getIssuedBy() != null ? driverCard.getIssuedBy() : dbDriverCard.getIssuedBy());
        dbDriverCard.setCategories(driverCard.getCategories() != null ? driverCard.getCategories() : dbDriverCard.getCategories());
        dbDriverCard.setBlock(driverCard.getBlock() != null ? driverCard.getBlock() : dbDriverCard.getBlock());
        dbDriverCard.setTypeCountry(driverCard.getTypeCountry() != null ? driverCard.getTypeCountry() : dbDriverCard.getTypeCountry());

        DriverCard savedCard = driverCardRepository.save(dbDriverCard);

        // Если ВУ стало заблокированным — деактивируем документы международного ВУ пользователя
        if (Boolean.TRUE.equals(savedCard.getBlock())) {
            deactivateInternationalDrivingDocuments(savedCard.getIdUser());
        }

        return savedCard;
    }

    @Audited(operation = "DEACTIVATE_DRIVER_CARD")
    public Map<String, Object> deactivateDriverCard(Long idDriverCard) {
        Map<String,Object> response = new HashMap<>();
        try{
            if(idDriverCard == null){
                response.put("status",100);
                response.put("message","Ошибка : ID удостоверения не может быть null");
                return response;
            }

            DriverCard driverCard = driverCardRepository.findByIdDriverCard(idDriverCard).orElseThrow(() -> new MainServiceException("Водительское удостоверение c ID " + idDriverCard + " не обнаружено"));

            if(driverCard == null){
                response.put("status",100);
                response.put("message","Ошибка : удостоверение с данным ID не найдено");
                return response;
            }


            driverCard.setBlock(true);

            driverCardRepository.save(driverCard);

            // Деактивируем активные документы типа международное ВУ (4, 5) у данного пользователя
            deactivateInternationalDrivingDocuments(driverCard.getIdUser());

            response.put("status",200);
            response.put("message","Успешно : пользователь деактивирован");
            return response;

        } catch (Exception e) {
            response.put("status",100);
            response.put("message", e.getMessage());
            return response;
        }
    }

    public DriverCard getDriverCardById(Long idDriverCard) {
        return driverCardRepository.findByIdDriverCard(idDriverCard).orElseThrow(() -> new MainServiceException("Водительское удостоверение c ID " + idDriverCard + " не обнаружено"));
    }


    public DriverCard getActualDriverCardByIdUser(Long idUser) {
        return driverCardRepository.findByIdUserAndBlock(idUser,false).orElseThrow(() -> new MainServiceException("Водительское удостоверение c ID User" + idUser + " не обнаружено"));
    }

    public List<DriverCard> getDriverCardsByIdUser(Long idUser) {
        return driverCardRepository.findAllByIdUser(idUser);

    }

    /**
     * При блокировке ВУ деактивируем (status → INACTIVE) все активные
     * документы типа международное ВУ (documentTypeId 4, 5) данного пользователя.
     */
    @Transactional
    void deactivateInternationalDrivingDocuments(Long idUser) {
        if (idUser == null) {
            return;
        }

        List<UserDocument> activeDocs =
                userDocumentRepository
                        .findAllByUserIdAndStatusOrderByValidUntilDesc(
                                idUser,
                                TechnicalInspectionStatus.ACTIVE
                        );

        List<UserDocument> intlDocs = activeDocs.stream()
                .filter(d -> d.getDocumentTypeId() == DOCUMENT_TYPE_INTERNATIONAL_VU
                        || d.getDocumentTypeId() == DOCUMENT_TYPE_INTERNATIONAL_VU_ALT)
                .collect(Collectors.toList());

        for (UserDocument doc : intlDocs) {
            doc.setStatus(TechnicalInspectionStatus.CANCELLED);
            doc.setUpdatedAt(LocalDateTime.now());
            userDocumentRepository.save(doc);
        }

        if (!intlDocs.isEmpty()) {
            log.info("Деактивированы документы международного ВУ (тип 4/5) для пользователя {}: {} шт.",
                    idUser, intlDocs.size());
        }
    }

    /**
     * Нормализация значения перед сравнением: убираем крайние пробелы и схлопываем повторяющиеся.
     */
    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }
}
