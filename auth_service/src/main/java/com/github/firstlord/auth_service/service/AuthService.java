package com.github.firstlord.auth_service.service;

import com.github.firstlord.auth_service.dto.RegisterRequest;
import com.github.firstlord.auth_service.enums.Role;
import com.github.firstlord.auth_service.exception.UserAlreadyExistsException;
import com.github.firstlord.auth_service.model.User;
import com.github.firstlord.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(RegisterRequest regRequest) {
        if (userRepository.findByUsername(regRequest.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken: " + regRequest.username());
        }

        if (userRepository.findByEmail(regRequest.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email is already taken: " + regRequest.email());
        }

        User user = new User();
        user.setUsername(regRequest.username());
        user.setPasswordHash(passwordEncoder.encode(regRequest.password()));
        user.setEmail(regRequest.email());
        user.setRole(Role.USER);

        if (StringUtils.hasText(regRequest.phone())) {
            user.setPhone(regRequest.phone());
        }

        return userRepository.save(user);
    }
}
