package dccargo.dcargoservice.controller;

import dccargo.dcargoservice.service.dcargo.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class UserController {

    private final UserService userService;


//    @GetMapping("/getUser")
//    public ResponseEntity<?> getPointsForDistribution(@RequestParam  shipmentDate,
//                                                      @RequestParam String typeSklad){
//
//    }

    @GetMapping("/getUsers")

//    @PostMapping("/createUser")
//
//    @PostMapping("/updateUser")

    @PostMapping("/deactivateUser")
    public ResponseEntity<?> deactivateUser(@RequestParam("idUser") Integer idUser){
        Map<String,Object> response = new HashMap<>();
        try {

            response = userService.deactivateUser(idUser);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
