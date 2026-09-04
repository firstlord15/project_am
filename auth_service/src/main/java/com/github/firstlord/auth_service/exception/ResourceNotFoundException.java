package com.github.firstlord.auth_service.exception;

/**
 * Запрошенный ресурс не найден.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
