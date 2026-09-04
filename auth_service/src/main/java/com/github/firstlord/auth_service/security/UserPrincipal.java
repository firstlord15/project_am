package com.github.firstlord.auth_service.security;

import com.github.firstlord.auth_service.enums.Role;
import com.github.firstlord.auth_service.model.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Обёртка над {@link User} для Spring Security.
 * Изолирует security-слой от JPA-сущности, наружу отдаёт только необходимое.
 */
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    /**
     * Идентификатор пользователя.
     */
    public UUID getId() {
        return user.getId();
    }

    /**
     * Роль пользователя.
     */
    public Role getRole() {
        return user.getRole();
    }
}