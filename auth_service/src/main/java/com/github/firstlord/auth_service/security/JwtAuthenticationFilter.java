package com.github.firstlord.auth_service.security;

import com.github.firstlord.auth_service.service.JwtTokenService;
import com.github.firstlord.auth_service.service.TokenRevocationService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр аутентификации по JWT из заголовка Authorization.
 * Проверяет тип токена, отзыв и подпись, наполняет SecurityContext.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final TokenRevocationService revocationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!tokenService.isAccessToken(token)) {
                log.debug("Rejected: refresh token used as access token");
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token type");
                return;
            }

            if (revocationService.isRevoked(token)) {
                log.debug("Rejected: token is revoked");
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token revoked");
                return;
            }

            String username = tokenService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (JwtException e) {
            log.debug("Rejected: invalid or expired token ({})", e.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired token");
            return;
        }

        chain.doFilter(request, response);
    }
}