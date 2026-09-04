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
 * Сервис административных операций над пользователями:
 * просмотр списка и деталей учётной записи.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Возвращает пользователя по идентификатору.
     *
     * @throws ResourceNotFoundException если пользователь с таким id не найден
     */
    public UserDTO getUserById(UUID id) {
        log.debug("Fetching user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        log.debug("User found: id={}, username={}", user.getId(), user.getUsername());
        return userMapper.toDTO(user);
    }

    /**
     * Возвращает постраничный список пользователей с опциональной фильтрацией
     * по роли, активности, блокировке и подстроке в username/email.
     * Параметры фильтра необязательны — {@code null}/пустое значение означает
     * отсутствие соответствующего условия.
     */
    public Page<UserDTO> getAllUsers(Role role, Boolean enabled, Boolean accountLocked,
                                     String search, Pageable pageable) {
        log.debug("Fetching users with filters: role={}, enabled={}, accountLocked={}, search={}, page={}",
                role, enabled, accountLocked, search, pageable);

        Specification<User> spec = Specification.allOf(
                UserSpecifications.hasRole(role),
                UserSpecifications.isEnabled(enabled),
                UserSpecifications.isLocked(accountLocked),
                UserSpecifications.search(search)
        );

        Page<UserDTO> result = userRepository.findAll(spec, pageable)
                .map(userMapper::toDTO);

        log.debug("Found {} users out of {} total", result.getNumberOfElements(), result.getTotalElements());
        return result;
    }
}