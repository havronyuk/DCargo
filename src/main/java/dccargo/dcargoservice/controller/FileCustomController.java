package dccargo.dcargoservice.controller;


import dccargo.dcargoservice.model.dcargo.Order;
import dccargo.dcargoservice.service.dcargo.FileCustomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class FileCustomController {

    private final FileCustomService fileCustomService;

    /**
     * Загружает файл и привязывает его к маршруту.
     *
     * @param file    файл, который загрузил пользователь
     * @param idObject идентификатор объекта
     * @param type тип файла
     * @return Map с результатом:
     *         - status = "200"
     *         - idFile = ID сохранённого файла
     * @throws IOException если произошла ошибка при сохранении файла
     */
    @PostMapping(
            value = "/uploadFile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Map<String, Object> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("idObject") Long idObject,
            @RequestParam("type") String type
    ) throws IOException {

        Map<String, Object> response = new HashMap<>();

        Long id = fileCustomService.saveFile(file, idObject, type);

        if(id != null){
            response.put("status",200);
            response.put("idFile", id);
            return response;
        }

        response.put("status", 100);
        response.put("message", "Ошибка сохранения файла");

        return response;
    }

    @GetMapping("/getIdFileList")
    public ResponseEntity<List<Long>> getIdFileList(@RequestParam Long idObject, @RequestParam String type){
        List<Long> idList = fileCustomService.getIdFileList(idObject,type);
        return ResponseEntity.ok(idList);
    }

    @GetMapping("/getFileById")
    public ResponseEntity<byte[]> getFileById(@RequestParam Long idFiles) {
        return fileCustomService.getFileById(idFiles);
    }

}
