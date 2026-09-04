package com.github.firstlord.auth_service.dto;

import com.github.firstlord.auth_service.enums.Role;
import com.github.firstlord.auth_service.model.User;

import java.util.UUID;

/**
 * Данные пользователя для отдачи наружу через API.
 * Не содержит хэш пароля.
 */
public record UserDTO(
        UUID id,
        String username,
        String email,
        String phone,
        Role role,
        boolean enabled,
        boolean accountLocked
) {}
