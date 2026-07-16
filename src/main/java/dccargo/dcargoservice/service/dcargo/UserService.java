package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.repository.dcargo.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user){
        if(userRepository.existsByLoginTelephoneAndBlockIsFalse(user.getLoginTelephone())){
            throw new MainServiceException("Пользователь с логином телефона " + user.getLoginTelephone() + " уже существует");
        }

        return userRepository.save(user);

    }

    /**
     * Метод обновления пользователя. <br> <b>Важно: проверяет все поля вручную!</b>
     * @param user
     * @return
     */
    @Transactional
    public User update(User user) {
        if (user.getIdUser() == null) {
            throw new MainServiceException("Отсутствует id в запросе");
        }

        User dbUser = userRepository.findById(user.getIdUser())
                .orElseThrow(() -> new MainServiceException("Пользователь c ID " + user.getIdUser() + " не обнаружен"));

        dbUser.setName(user.getName() != null ? user.getName() : dbUser.getName());
        dbUser.setSurname(user.getSurname() != null ? user.getSurname() : dbUser.getSurname());
        dbUser.setPatronymic(user.getPatronymic() != null ? user.getPatronymic() : dbUser.getPatronymic());
        dbUser.setLogin(user.getLogin() != null ? user.getLogin() : dbUser.getLogin());
        dbUser.setPassword(user.getPassword() != null ? user.getPassword() : dbUser.getPassword());
        dbUser.setLoginTelephone(user.getLoginTelephone() != null ? user.getLoginTelephone() : dbUser.getLoginTelephone());
        dbUser.setTelephone(user.getTelephone() != null ? user.getTelephone() : dbUser.getTelephone());
        dbUser.setEnablet(user.getEnablet() != null ? user.getEnablet() : dbUser.getEnablet());
        dbUser.setDepartment(user.getDepartment() != null ? user.getDepartment() : dbUser.getDepartment());
        dbUser.setEmail(user.getEmail() != null ? user.getEmail() : dbUser.getEmail());
        dbUser.setNumYNP(user.getNumYNP() != null ? user.getNumYNP() : dbUser.getNumYNP());
        dbUser.setLoyalty(user.getLoyalty() != null ? user.getLoyalty() : dbUser.getLoyalty());
        dbUser.setStatus(user.getStatus() != null ? user.getStatus() : dbUser.getStatus());
        dbUser.setBlock(user.getBlock() != null ? user.getBlock() : dbUser.getBlock());
        dbUser.setIp(user.getIp() != null ? user.getIp() : dbUser.getIp());
        dbUser.setChatId(user.getChatId() != null ? user.getChatId() : dbUser.getChatId());

        // date_create не обновляем, оставляем оригинальную дату создания

        return userRepository.save(dbUser);
    }


    public Map<String, Object> deactivateUser(Integer idUser) {
        Map<String,Object> response = new HashMap<>();
        try{
            if(idUser == null){
                response.put("status",100);
                response.put("message","Ошибка : ID пользователя не может быть null");
                return response;
            }

            User user = userRepository.findByIdUser(idUser);

            if(user == null){
                response.put("status",100);
                response.put("message","Ошибка : пользователен с данным ID не найден");
                return response;
            }


            user.setBlock(true);

            userRepository.save(user);

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
