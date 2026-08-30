package com.github.firstlord.auth_service.dto;

import lombok.Data;

import java.util.UUID;

public record AuthResponse (
        String accessToken,
        String tokenType,
        long expiresIn,
        UUID userId,
        String username,
        String role
) {}
