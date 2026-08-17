package dccargo.dcargoservice.controller;

import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.model.dcargo.UserDocType;
import dccargo.dcargoservice.model.dcargo.UserDocument;
import dccargo.dcargoservice.service.dcargo.UserDocTypeService;
import dccargo.dcargoservice.service.dcargo.UserDocumentService;
import dccargo.dcargoservice.service.dcargo.UserService;
import dccargo.dcargoservice.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class UserController {

    private final UserService userService;
    private final UserDocTypeService userDocTypeService;
    private final UserDocumentService userDocumentService;
    private final SecurityUtils securityUtils;


    @GetMapping("/getUserById")
    public ResponseEntity<User> getUserById(@RequestParam Long idUser){
        User user = userService.getUserById(idUser);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println(securityUtils.getCurrentUserId());
        // equipmentType.setCreatedByUserId(SecurityUtils.getCurrentUserId());

        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getAllActiveUsers")
    public ResponseEntity<List<User>> getAllActiveUsers() {
        List<User> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }


    @PostMapping("/createUser")
    public ResponseEntity<User> create(@RequestBody User user) {
        User savedUser = userService.createUser(user);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<User> update(@RequestBody User user) {

        User savedUser = userService.update(user);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/deactivateUser")
    public ResponseEntity<?> deactivateUser(@RequestParam("idUser") Long idUser){
        Map<String,Object> response = new HashMap<>();
        try {

            response = userService.deactivateUser(idUser);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /*
     * =============ТИПЫ ДОКУМЕНТОВ ПОЛЬЗОВАТЕЛЕЙ=================
     */
    @GetMapping("/getAllUserDocType")
    public ResponseEntity<List<UserDocType>> getAllUserDocType() {
        return ResponseEntity.ok(userDocTypeService.getAll());
    }

    @PostMapping("/createUserDocType")
    public ResponseEntity<UserDocType> createUserDocType(@RequestBody UserDocType userDocType) {
        log.info("Создание типа документа пользователя.");
        UserDocType saved = userDocTypeService.create(userDocType);
        return ResponseEntity.ok(saved);
    }

    /*
     * =============ДОКУМЕНТЫ ПОЛЬЗОВАТЕЛЕЙ=================
     */
    @GetMapping("/getUserDocument/{userId}")
    public ResponseEntity<List<UserDocument>> getUserDocument(@PathVariable Long userId) {
        List<UserDocument> documents = userDocumentService.getByUserId(userId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/getAllUserDocument")
    public ResponseEntity<List<UserDocument>> getAllUserDocument() {
        return ResponseEntity.ok(userDocumentService.getAll());
    }

    @PostMapping("/createUserDocument")
    public ResponseEntity<UserDocument> createUserDocument(@RequestBody UserDocument userDocument) {
        log.info("Создание документа пользователя. UserId: {}", userDocument.getUserId());
        UserDocument saved = userDocumentService.create(userDocument);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/updateUserDocument")
    public ResponseEntity<UserDocument> updateUserDocument(@RequestBody UserDocument userDocument) {
        log.info("Обновление документа пользователя. ID: {}", userDocument.getId());
        UserDocument updated = userDocumentService.update(userDocument);
        return ResponseEntity.ok(updated);
    }

}
