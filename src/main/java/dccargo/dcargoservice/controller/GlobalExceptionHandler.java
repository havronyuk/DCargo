package dccargo.dcargoservice.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;

/**
 * Глобальный обработчик ошибок. Экранирует 500 ошибки
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MainServiceException.class) // указываем какой класс ошибок отлавливаем
    public ResponseEntity<Map<String, Object>> handleDuplicateTruck(
            MainServiceException exception) {

        Map<String, Object> response = Map.of(
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflict",
                "message", exception.getMessage(),
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

}
