package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.model.dcargo.FileCustom;
import dccargo.dcargoservice.repository.dcargo.FileCustomRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileCustomService {

    private final FileCustomRepository fileCustomRepository;



    @Transactional
    public Long saveFile(MultipartFile file, Long idObject, String type) throws IOException {

        try{
            FileCustom entity = new FileCustom();

            entity.setFileName(file.getOriginalFilename());

            entity.setContentType(file.getContentType());

            entity.setData(file.getBytes());

            entity.setDateCreate(Timestamp.from(Instant.now()));

            // Здесь пока заполним вручную или заглушками

//            entity.setUserId(user.getIdUser()); // можно получить из авторизации или формы
//
//            entity.setUserName(user.getSurname() + " " + user.getName());
//
//            entity.setUserCompanyName(user.getCompanyName());
//
//            entity.setUserEmail(user.geteMail());

            entity.setStatus(1);

            entity.setIdObject(idObject);

            entity.setType(type);

            long sizeInBytes = file.getSize();

            BigDecimal sizeInMB = BigDecimal.valueOf(sizeInBytes)
                    .divide(BigDecimal.valueOf(1024 * 1024), 2, RoundingMode.HALF_UP);

            String sizeFormatted = String.format("%.2f MB", sizeInMB);

            entity.setSize(sizeInMB);

            entity.setSizeType("МБ");

            entity.setFromSystem("Yard");

            return fileCustomRepository.save(entity).getIdFiles();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MainServiceException("Ошибка сохранения файла");

        }
    }


    public List<Long> getIdFileList(Long idObject, String type) {
        return fileCustomRepository.getIdFilesByIdObjectAndType(idObject, type);
    }

    public ResponseEntity<byte[]> getFileById(Long idFiles) {
        FileCustom file = fileCustomRepository.getByIdFiles(idFiles);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = file.getContentType();
        String fileName = file.getFileName();

        if (contentType == null || contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        String disposition = "attachment";
        if (fileName != null && !fileName.isBlank()) {
            String safeName = fileName.replaceAll("[\"\\\\\r\n]", "_");
            String encoded = URLEncoder.encode(safeName, StandardCharsets.UTF_8)
                    .replace("+", "%20")
                    .replace("%2F", "/");
            disposition = "attachment; filename=\"" + safeName + "\"; filename*=UTF-8''" + encoded;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .body(file.getData());
    }

}
