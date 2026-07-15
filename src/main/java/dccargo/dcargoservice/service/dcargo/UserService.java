package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.repository.dcargo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class UserService {

    private final UserRepository userRepository;


}
