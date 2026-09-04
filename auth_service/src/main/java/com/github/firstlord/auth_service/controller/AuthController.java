package com.github.firstlord.auth_service.controller;

import com.github.firstlord.auth_service.dto.AuthResponse;
import com.github.firstlord.auth_service.dto.LoginRequest;
import com.github.firstlord.auth_service.dto.RegisterRequestDTO;
import com.github.firstlord.auth_service.model.User;
import com.github.firstlord.auth_service.security.UserPrincipal;
import com.github.firstlord.auth_service.service.AuthService;
import com.github.firstlord.auth_service.service.JwtTokenService;
import com.github.firstlord.auth_service.service.TokenRevocationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Objects;

/**
 * Контроллер аутентификации: регистрация, вход, обновление и отзыв токенов.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${jwt.access-token-ttl-seconds}")
    private long expiresIn;

    private final AuthenticationManager authManager;
    private final JwtTokenService tokenService;
    private final TokenRevocationService revocationService;
    private final UserDetailsService userDetailsService;
    private final AuthService authService;

    /**
     * Регистрирует нового пользователя и сразу выдаёт пару токенов.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequestDTO request, HttpServletResponse response) {
        log.debug("Registration request received: username={}", request.username());

        User user = authService.register(request);
        UserPrincipal principal = new UserPrincipal(user);

        String accessToken = tokenService.generateAccessToken(principal);
        String refreshToken = tokenService.generateRefreshToken(principal.getUsername());

        addRefreshTokenCookie(response, refreshToken);

        log.info("User registered: userId={}, username={}", principal.getId(), principal.getUsername());

        return new AuthResponse(
                accessToken,
                "Bearer",
                expiresIn,
                principal.getId(),
                principal.getUsername(),
                principal.getRole().toString()
        );
    }

    /**
     * Аутентифицирует пользователя по логину и паролю, выдаёт пару токенов.
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest req, HttpServletResponse response) {
        log.debug("Login attempt: username={}", req.username());

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        String accessToken = tokenService.generateAccessToken(Objects.requireNonNull(principal));
        String refreshToken = tokenService.generateRefreshToken(principal.getUsername());

        addRefreshTokenCookie(response, refreshToken);

        log.info("User logged in: userId={}, username={}", principal.getId(), principal.getUsername());

        return new AuthResponse(
                accessToken,
                "Bearer",
                expiresIn,
                principal.getId(),
                principal.getUsername(),
                principal.getRole().toString()
        );
    }

    /**
     * Выдаёт новый access-токен по действующему refresh-токену.
     */
    @PostMapping("/refresh")
    public AuthResponse refresh(@CookieValue("refreshToken") String refreshToken) {
        log.debug("Refresh token request received");

        if (revocationService.isRevoked(refreshToken)) {
            log.warn("Refresh rejected: token is revoked");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token revoked");
        }

        if (tokenService.isAccessToken(refreshToken)) {
            log.warn("Refresh rejected: access token used instead of refresh token");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token type");
        }

        String username = tokenService.extractUsername(refreshToken);
        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

        String accessToken = tokenService.generateAccessToken(principal);

        log.debug("Access token refreshed: username={}", username);

        return new AuthResponse(
                accessToken,
                "Bearer",
                expiresIn,
                principal.getId(),
                principal.getUsername(),
                principal.getRole().toString()
        );
    }

    /**
     * Отзывает access- и refresh-токены и удаляет refresh-cookie на клиенте.
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                       @RequestHeader(value = "Authorization", required = false) String authHeader,
                       HttpServletResponse response) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            revocationService.revoke(authHeader.substring(7));
        }

        if (refreshToken != null) {
            revocationService.revoke(refreshToken);
        }

        log.info("User logged out, tokens revoked");

        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
    }

    /**
     * Кладёт refresh-токен в HttpOnly-cookie, недоступную из JavaScript.
     */
    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}