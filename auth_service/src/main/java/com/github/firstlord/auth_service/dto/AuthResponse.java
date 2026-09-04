package com.github.firstlord.auth_service.dto;

import java.util.UUID;

/**
 * Ответ на успешную аутентификацию или регистрацию.
 * Refresh-токен в этом ответе не передаётся — он уходит в HttpOnly-cookie.
 */
public record AuthResponse (
        String accessToken,
        String tokenType,
        long expiresIn,
        UUID userId,
        String username,
        String role
) {}
