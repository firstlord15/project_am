package com.github.firstlord.auth_service.repository;

import com.github.firstlord.auth_service.enums.Role;
import com.github.firstlord.auth_service.model.User;
import org.springframework.data.jpa.domain.Specification;

/**
 * Динамические условия фильтрации пользователей для админского списка.
 * Каждый метод возвращает спецификацию, не влияющую на выборку,
 * если соответствующий параметр не передан (null/пусто).
 */
public class UserSpecifications {

    /**
     * Фильтр по роли пользователя. Не применяется, если {@code role} не задан.
     */
    public static Specification<User> hasRole(Role role) {
        return (root, query, cb) -> role == null ? null : cb.equal(root.get("role"), role);
    }

    /**
     * Фильтр по признаку активности учётной записи. Не применяется, если {@code enabled} не задан.
     */
    public static Specification<User> isEnabled(Boolean enabled) {
        return (root, query, cb) -> enabled == null ? null : cb.equal(root.get("enabled"), enabled);
    }

    /**
     * Фильтр по признаку блокировки учётной записи. Не применяется, если {@code locked} не задан.
     */
    public static Specification<User> isLocked(Boolean locked) {
        return (root, query, cb) -> locked == null ? null : cb.equal(root.get("accountLocked"), locked);
    }

    /**
     * Поиск по подстроке в username или email (регистронезависимый).
     * Не применяется, если {@code term} пустой или не задан.
     */
    public static Specification<User> search(String term) {
        if (term == null || term.isBlank()) {
            return Specification.unrestricted();
        }
        String pattern = "%" + term.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("username")), pattern),
                cb.like(cb.lower(root.get("email")), pattern)
        );
    }
}