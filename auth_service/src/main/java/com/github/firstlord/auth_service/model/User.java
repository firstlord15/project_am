package com.github.firstlord.auth_service.model;

import com.github.firstlord.auth_service.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * Модель пользователя.
 */

@Data
@Slf4j
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Логин пользователя, используется для входа в систему.
     * Уникален в рамках всей системы.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Хэш пароля пользователя.
     */
    @Column(nullable = false)
    private String passwordHash;

    /**
     * Адрес электронной почты пользователя.
     * Уникален в рамках всей системы.
     * Может использоваться для восстановления доступа и уведомлений.
     */
    @Column(unique = true)
    private String email;

    /**
     * Номер телефона пользователя.
     * Необязательное поле, дополнительный контактный канал.
     */
    private String phone;

    /**
     * Роль пользователя в системе, определяет набор его прав доступа.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Признак активности учётной записи.
     * {@code false} означает, что пользователь деактивирован
     * и не может пройти аутентификацию.
     */
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * Признак блокировки учётной записи.
     * {@code true} означает, что аккаунт заблокирован администратором
     * или системой и не может пройти аутентификацию.
     */
    @Column(nullable = false)
    private boolean accountLocked = false;
}

