package com.github.firstlord.auth_service.exception;

/**
 * Пользователь с таким username или email уже зарегистрирован.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
