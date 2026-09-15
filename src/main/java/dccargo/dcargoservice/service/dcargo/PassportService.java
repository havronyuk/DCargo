package dccargo.dcargoservice.service.dcargo;

import dccargo.dcargoservice.audit.Audited;
import dccargo.dcargoservice.model.dcargo.Passport;
import dccargo.dcargoservice.repository.dcargo.PassportRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class PassportService {

    private final PassportRepository passportRepository;


    public Passport create(Passport passport) {

        if(passport.getIdPassport() != null){
            throw new MainServiceException("Нельзя создать запись, если ID уже существует");
        }

        if(passport.getIdUser() == null){
            throw new MainServiceException("Нельзя создать запись без привязки к пользователю");
        }

        boolean existActual = passportRepository.existsByIdUserAndBlock(passport.getIdUser(),false);

        if(existActual){
            throw new MainServiceException("Создание новой записи запрещено. Заблокируйте актуальную запись");
        }

//        if(passport.getNumber() != null && passport.getSeries() != null){
//
//        }


        if(passport.getPersonalNumber() != null){
            List<Passport> passportsWithNumber = passportRepository.findAllByPersonalNumber(passport.getPersonalNumber());
            boolean belongsToOtherUser = passportsWithNumber.stream()
                    .anyMatch(p -> !p.getIdUser().equals(passport.getIdUser()));
            if(belongsToOtherUser){
                throw new MainServiceException("Паспорт с данным идентификационным  номером уже существует");
            }
        }

        // Идентификационный номер должен быть всегда один у пользователя
        // при смене между Паспорт РБ и ID-карта РБ (и наоборот),
        // Паспорт РБ и Паспорт РБ, ID-карта РБ и ID-карта РБ.
        // Вид на жительство и прочие типы — не проверяем.
        List<Passport> userPassports = passportRepository.findAllByIdUser(passport.getIdUser());
        String previousNumber = userPassports.stream()
                .filter(p -> isPassportOrIdCard(p.getType()))
                .map(Passport::getPersonalNumber)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if(previousNumber != null
                && passport.getPersonalNumber() != null
                && !normalize(previousNumber).equals(normalize(passport.getPersonalNumber()))){
            throw new MainServiceException(
                    "Идентификационный номер не соответствует номеру ранее выданного документа пользователя. "
                            + "При смене Паспорт РБ / ID-карта РБ номер должен оставаться прежним"
            );
        }

        return passportRepository.save(passport);
    }

    /**
     * Нормализация значения перед сравнением: убираем крайние пробелы и схлопываем повторяющиеся.
     */
    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }

    /**
     * Принадлежит ли тип документа к группе «Паспорт РБ / ID-карта РБ»,
     * для которой действует правило единообразия идентификационного номера.
     */
    private boolean isPassportOrIdCard(String type) {
        return type != null
                && ("Паспорт РБ".equals(type.trim())
                    || "ID-карта РБ".equals(type.trim()));
    }


    /**
     * Метод обновления паспорта. <br> <b>Важно: проверяет все поля вручную!</b>
     * @param passport
     * @return
     */
    @Audited(operation = "UPDATE_PASSPORT")
    @Transactional
    public Passport update(Passport passport) {
        if (passport.getIdPassport() == null) {
            throw new MainServiceException("Отсутствует id в запросе");
        }

        Passport dbPassport = passportRepository.findById(passport.getIdPassport())
                .orElseThrow(() -> new MainServiceException("Паспорт c ID " + passport.getIdPassport() + " не обнаружен"));

        if(dbPassport.getIdUser() != passport.getIdUser()){
            throw new MainServiceException("Нельзя передать паспорт другому пользователя");
        }

        dbPassport.setSeries(passport.getSeries() != null ? passport.getSeries() : dbPassport.getSeries());
        dbPassport.setNumber(passport.getNumber() != null ? passport.getNumber() : dbPassport.getNumber());
        dbPassport.setPersonalNumber(passport.getPersonalNumber() != null ? passport.getPersonalNumber() : dbPassport.getPersonalNumber());
        dbPassport.setIssueDate(passport.getIssueDate() != null ? passport.getIssueDate() : dbPassport.getIssueDate());
        dbPassport.setExpiryDate(passport.getExpiryDate() != null ? passport.getExpiryDate() : dbPassport.getExpiryDate());
        dbPassport.setIssuedBy(passport.getIssuedBy() != null ? passport.getIssuedBy() : dbPassport.getIssuedBy());
        dbPassport.setType(passport.getType() != null ? passport.getType() : dbPassport.getType());


        Passport updatedPassport = passportRepository.save(dbPassport);
        log.info("Обновлен паспорт. ID: {}, Серия: {}, Номер: {}",
                updatedPassport.getIdPassport(),
                updatedPassport.getSeries(),
                updatedPassport.getNumber());

        return updatedPassport;
    }

    @Audited(operation = "DEACTIVATE_PASSPORT")
    public Map<String, Object> deactivatePassport(Long idPassport) {
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


    public Passport getPassportById(Long idPassport) {
        return passportRepository.findByIdPassport(idPassport);
    }


    public Passport getActualPassportByIdUser(Long idUser) {
        return passportRepository.findByIdUserAndBlock(idUser,false);
    }

    public List<Passport> getPassportsByIdUser(Long idUser) {
        return passportRepository.findAllByIdUser(idUser);

    }
}
