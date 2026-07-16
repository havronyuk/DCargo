package dccargo.dcargoservice.service.dcargo;

import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.repository.dcargo.PassportRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class PassportService {

    private final PassportRepository passportRepository;


    public Passport create(Passport passport) {

        if(passport.getIdPassport() != null){
            throw new MainServiceException("Нельзя создать запись, если ID уже существует");
        }

        if(passportRepository.existsByPersonalNumber(passport.getPersonalNumber())){
            throw new MainServiceException("Паспорт с данным идентификационным  номером уже существует");
        }


        return passportRepository.save(passport);
    }


    /**
     * Метод обновления паспорта. <br> <b>Важно: проверяет все поля вручную!</b>
     * @param passport
     * @return
     */
    @Transactional
    public Passport update(Passport passport) {
        if (passport.getIdPassport() == null) {
            throw new MainServiceException("Отсутствует id в запросе");
        }

        Passport dbPassport = passportRepository.findById(passport.getIdPassport())
                .orElseThrow(() -> new MainServiceException("Паспорт c ID " + passport.getIdPassport() + " не обнаружен"));

        dbPassport.setSeries(passport.getSeries() != null ? passport.getSeries() : dbPassport.getSeries());
        dbPassport.setNumber(passport.getNumber() != null ? passport.getNumber() : dbPassport.getNumber());
        dbPassport.setPersonalNumber(passport.getPersonalNumber() != null ? passport.getPersonalNumber() : dbPassport.getPersonalNumber());
        dbPassport.setIssueDate(passport.getIssueDate() != null ? passport.getIssueDate() : dbPassport.getIssueDate());
        dbPassport.setExpiryDate(passport.getExpiryDate() != null ? passport.getExpiryDate() : dbPassport.getExpiryDate());
        dbPassport.setIssuedBy(passport.getIssuedBy() != null ? passport.getIssuedBy() : dbPassport.getIssuedBy());

        Passport updatedPassport = passportRepository.save(dbPassport);
        log.info("Обновлен паспорт. ID: {}, Серия: {}, Номер: {}",
                updatedPassport.getIdPassport(),
                updatedPassport.getSeries(),
                updatedPassport.getNumber());

        return updatedPassport;
    }

    public Map<String, Object> deactivatePassport(Integer idPassport) {
        Map<String,Object> response = new HashMap<>();
        try{
            if(idPassport == null){
                response.put("status",100);
                response.put("message","Ошибка : ID паспорта не может быть null");
                return response;
            }

            Passport passport = passportRepository.findByIdPassport(idPassport);

            if(passport == null){
                response.put("status",100);
                response.put("message","Ошибка : паспорт с данным ID не найден");
                return response;
            }


            passport.setBlock(true);

            passportRepository.save(passport);

            response.put("status",200);
            response.put("message","Успешно : пользователь деактивирован");
            return response;

        } catch (Exception e) {
            response.put("status",100);
            response.put("message", e.getMessage());
            return response;
        }
    }



}
