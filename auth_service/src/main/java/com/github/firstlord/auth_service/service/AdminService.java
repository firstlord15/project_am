package com.github.firstlord.auth_service.service;

import com.github.firstlord.auth_service.dto.UserDTO;
import com.github.firstlord.auth_service.enums.Role;
import com.github.firstlord.auth_service.exception.ResourceNotFoundException;
import com.github.firstlord.auth_service.mapper.UserMapper;
import com.github.firstlord.auth_service.model.User;
import com.github.firstlord.auth_service.repository.UserRepository;
import com.github.firstlord.auth_service.repository.UserSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Сервис админ панели.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getUserById(UUID id) {
        log.debug("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        log.debug("User found: id={}, username={}", user.getId(), user.getUsername());
        return userMapper.toDTO(user);
    }

    public Page<UserDTO> getAllUsers(Role role, Boolean enabled,
            Boolean accountLocked, String search, Pageable pageable) {

        log.debug("Fetching users with filters: role={}, enabled={}, accountLocked={}, search={}, page={}", role, enabled, accountLocked, search, pageable);
        Specification<User> spec = Specification
                .where(UserSpecifications.hasRole(role))
                .and(UserSpecifications.isEnabled(enabled))
                .and(UserSpecifications.isLocked(accountLocked))
                .and(UserSpecifications.search(search));

        Page<UserDTO> result = userRepository.findAll(spec, pageable)
                .map(userMapper::toDTO);

        log.debug("Found {} users out of {} total", result.getNumberOfElements(), result.getTotalElements());
        return result;
    }
}
