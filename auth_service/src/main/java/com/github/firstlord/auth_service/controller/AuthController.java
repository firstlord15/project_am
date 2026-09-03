package com.github.firstlord.auth_service.controller;

import com.github.firstlord.auth_service.dto.AuthResponse;
import com.github.firstlord.auth_service.dto.LoginRequest;
import com.github.firstlord.auth_service.dto.RegisterRequest;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        User user = authService.register(request);
        UserPrincipal principal = new UserPrincipal(user);

        String accessToken = tokenService.generateAccessToken(principal);
        String refreshToken = tokenService.generateRefreshToken(principal.getUsername());

        addRefreshTokenCookie(response, refreshToken);

        return new AuthResponse(
                accessToken,
                "Bearer",
                expiresIn,
                principal.getId(),
                principal.getUsername(),
                principal.getRole().toString()
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest req, HttpServletResponse response) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        String accessToken = tokenService.generateAccessToken(Objects.requireNonNull(principal));
        String refreshToken = tokenService.generateRefreshToken(principal.getUsername());

        addRefreshTokenCookie(response, refreshToken);

        return new AuthResponse(
                accessToken,
                "Bearer",
                expiresIn,
                principal.getId(),
                principal.getUsername(),
                principal.getRole().toString()
        );
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@CookieValue("refreshToken") String refreshToken) {
        if (revocationService.isRevoked(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token revoked");
        }

        if (tokenService.isAccessToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token type");
        }

        String username = tokenService.extractUsername(refreshToken);
        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

        String accessToken = tokenService.generateAccessToken(principal);

        return new AuthResponse(
                accessToken,
                "Bearer",
                expiresIn,
                principal.getId(),
                principal.getUsername(),
                principal.getRole().toString()
        );
    }

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

        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
    }

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
