package com.github.firstlord.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Данные для регистрации нового пользователя.
 */
public record RegisterRequestDTO(
        /*
          Логин будущего пользователя. От 3 до 100 символов,
          только латиница, цифры, точка, подчёркивание и дефис.
         */
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "Username can only contain letters, digits, dots, underscores and hyphens")
        String username,

        /*
          Пароль в открытом виде. Хэшируется на сервере перед сохранением,
          никогда не хранится и не логируется как есть.
         */
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        /*
          Адрес электронной почты. Должен быть уникален в системе.
         */
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        /*
          Номер телефона. Необязательное поле.
         */
        String phone
) {}
