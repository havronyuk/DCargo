package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.model.dcargo.DriverCard;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.repository.dcargo.DriverCardRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class DriverCardService {

    private final DriverCardRepository driverCardRepository;

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

        if(driverCardRepository.existsByNumber(driverCard.getNumber())){
            throw new MainServiceException("Водительское удостоверение с данным номером уже существует");
        }

        return driverCardRepository.save(driverCard);
    }

    /**
     * Метод обновления водительского удостоверения. <br> <b>Важно: проверяет все поля вручную!</b>
     * @param driverCard
     * @return
     */
    @Transactional
    public DriverCard update(DriverCard driverCard) {
        if (driverCard.getIdDriverCard() == null) {
            throw new MainServiceException("Отсутствует id в запросе");
        }

        DriverCard dbDriverCard = driverCardRepository.findById(driverCard.getIdDriverCard())
                .orElseThrow(() -> new MainServiceException("Водительское удостоверение c ID " + driverCard.getIdDriverCard() + " не обнаружено"));

        if(dbDriverCard.getIdUser() != driverCard.getIdUser()){
            throw new MainServiceException("Нельзя передать удостоверение на другого пользователя");
        }

        dbDriverCard.setNumber(driverCard.getNumber() != null ? driverCard.getNumber() : dbDriverCard.getNumber());
        dbDriverCard.setIssueDate(driverCard.getIssueDate() != null ? driverCard.getIssueDate() : dbDriverCard.getIssueDate());
        dbDriverCard.setExpiryDate(driverCard.getExpiryDate() != null ? driverCard.getExpiryDate() : dbDriverCard.getExpiryDate());
        dbDriverCard.setIssuedBy(driverCard.getIssuedBy() != null ? driverCard.getIssuedBy() : dbDriverCard.getIssuedBy());
        dbDriverCard.setCategories(driverCard.getCategories() != null ? driverCard.getCategories() : dbDriverCard.getCategories());
        dbDriverCard.setBlock(driverCard.getBlock() != null ? driverCard.getBlock() : dbDriverCard.getBlock());

        return driverCardRepository.save(dbDriverCard);
    }

    public Map<String, Object> deactivateDriverCard(Integer idDriverCard) {
        Map<String,Object> response = new HashMap<>();
        try{
            if(idDriverCard == null){
                response.put("status",100);
                response.put("message","Ошибка : ID удостоверения не может быть null");
                return response;
            }

            DriverCard driverCard = driverCardRepository.findByIdDriverCard(idDriverCard);

            if(driverCard == null){
                response.put("status",100);
                response.put("message","Ошибка : удостоверение с данным ID не найдено");
                return response;
            }


            driverCard.setBlock(true);

            driverCardRepository.save(driverCard);

            response.put("status",200);
            response.put("message","Успешно : пользователь деактивирован");
            return response;

        } catch (Exception e) {
            response.put("status",100);
            response.put("message", e.getMessage());
            return response;
        }
    }

    public DriverCard getDriverCardById(Integer idDriverCard) {
        return driverCardRepository.findByIdDriverCard(idDriverCard);
    }


    public DriverCard getActualDriverCardByIdUser(Integer idUser) {
        return driverCardRepository.findByIdUserAndBlock(idUser,false);
    }

    public List<DriverCard> getDriverCardsByIdUser(Integer idUser) {
        return driverCardRepository.findAllByIdUser(idUser);

    }
}
