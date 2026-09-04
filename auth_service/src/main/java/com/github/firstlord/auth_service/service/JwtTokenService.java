package com.github.firstlord.auth_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Сервис генерации и валидации JWT.
 * Алгоритм: HS256 (симметричный) — общий секрет с сервисами-потребителями токена.
 */
@Slf4j
@Service
public class JwtTokenService {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Формирует ключ подписи из секрета, заданного в конфигурации.
     */
    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Генерирует access-токен со сроком жизни 15 минут.
     */
    public String generateAccessToken(UserDetails user) {
        log.debug("Generating access token: username={}", user.getUsername());

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 min
                .signWith(signingKey())
                .compact();
    }

    /**
     * Генерирует refresh-токен со сроком жизни 7 дней.
     */
    public String generateRefreshToken(String username) {
        log.debug("Generating refresh token: username={}", username);

        return Jwts.builder()
                .subject(username)
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L)) // 7 days
                .signWith(signingKey())
                .compact();
    }

    /**
     * Проверяет подпись токена и возвращает его claims.
     * Бросает {@link io.jsonwebtoken.JwtException}, если токен просрочен,
     * повреждён или подписан неверным ключом.
     */
    public Claims validateAndParse(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Извлекает username (subject) из токена.
     */
    public String extractUsername(String token) {
        return validateAndParse(token).getSubject();
    }

    /**
     * Проверяет, является ли токен access-токеном.
     */
    public boolean isAccessToken(String token) {
        Claims claims = validateAndParse(token);
        return !"refresh".equals(claims.get("type"));
    }
}
