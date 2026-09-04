package com.github.firstlord.auth_service.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Сервис отзыва токенов через Redis.
 * Токен хранится как ключ до момента истечения его собственного срока действия —
 * после этого запись из Redis удаляется автоматически (TTL) и хранить её дальше не нужно.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenRevocationService {

    private final JwtTokenService jwtTokenService;
    private final RedisTemplate<String, String> redis;
    private static final String PREFIX = "revoked:";

    /**
     * Помечает токен как отозванный. Запись в Redis живёт ровно до истечения
     * срока действия самого токена — дальше отзыв уже не нужен.
     */
    public void revoke(String token) {
        Claims claims = jwtTokenService.validateAndParse(token);
        long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            redis.opsForValue().set(
                    PREFIX + token,
                    "revoked",
                    Duration.ofMillis(ttl)
            );
        }
    }

    /**
     * Проверяет, был ли токен отозван.
     */
    public boolean isRevoked(String token) {
        return Boolean.TRUE.equals(redis.hasKey(PREFIX + token));
    }
}
