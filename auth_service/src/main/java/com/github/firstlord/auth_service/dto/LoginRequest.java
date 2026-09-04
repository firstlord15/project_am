package com.github.firstlord.auth_service.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Данные для входа в систему.
 */
public record LoginRequest (
        @NotBlank String username,
        @NotBlank String password
) {}
