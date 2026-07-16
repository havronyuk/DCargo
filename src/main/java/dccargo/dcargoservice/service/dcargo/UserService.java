package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.repository.dcargo.UserRepository;
import dccargo.dcargoservice.service.dcargo.exception.TruckException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user){
        if(userRepository.existsByLoginTelephoneAndBlockIsFalse(user.getLoginTelephone())){
            throw new TruckException("Пользователь с логином телефона " + user.getLoginTelephone() + " уже существует");
        }

        return userRepository.save(user);

    }

    public User update(User user){
        if(!userRepository.existsByIdUser(user.getIdUser())){
            throw new TruckException("Пользователь c ID " + user.getIdUser() + " не обнаружен");
        }

        return userRepository.save(user);

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
