package com.github.firstlord.auth_service.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenRevocationService {

    private final JwtTokenService jwtTokenService;
    private final RedisTemplate<String, String> redis;
    private static final String PREFIX = "revoked:";

    // Store the token JTI (JWT ID) in Redis until expiry
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

    public boolean isRevoked(String token) {
        return Boolean.TRUE.equals(redis.hasKey(PREFIX + token));
    }
}
