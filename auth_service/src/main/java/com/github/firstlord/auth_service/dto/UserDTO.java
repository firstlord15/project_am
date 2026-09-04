package com.github.firstlord.auth_service.dto;

import com.github.firstlord.auth_service.enums.Role;
import com.github.firstlord.auth_service.model.User;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email,
        String phone,
        Role role,
        boolean enabled,
        boolean accountLocked
) {
    public static UserDTO from(User user) {
        return new UserDTO(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPhone(), user.getRole(), user.isEnabled(), user.isAccountLocked()
        );
    }
}
