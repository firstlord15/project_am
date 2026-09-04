package com.github.firstlord.auth_service.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Единый формат тела ответа при ошибке.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    /**
     * Время возникновения ошибки.
     */
    private LocalDateTime timestamp;

    /**
     * HTTP-статус ответа.
     */
    private int status;

    /**
     * Краткое название ошибки (например, "Unauthorized").
     */
    private String error;

    /**
     * Человеко-читаемое описание причины ошибки.
     */
    private String message;

    /**
     * Путь запроса, на котором произошла ошибка.
     */
    private String path;
}