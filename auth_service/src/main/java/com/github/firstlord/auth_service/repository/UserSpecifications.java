package com.github.firstlord.auth_service.repository;

import com.github.firstlord.auth_service.enums.Role;
import com.github.firstlord.auth_service.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> hasRole(Role role) {
        return (root, query, cb) -> role == null ? null : cb.equal(root.get("role"), role);
    }

    public static Specification<User> isEnabled(Boolean enabled) {
        return (root, query, cb) -> enabled == null ? null : cb.equal(root.get("enabled"), enabled);
    }

    public static Specification<User> isLocked(Boolean locked) {
        return (root, query, cb) -> locked == null ? null : cb.equal(root.get("accountLocked"), locked);
    }

    public static Specification<User> search(String term) {
        if (term == null || term.isBlank()) return null;
        String pattern = "%" + term.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("username")), pattern),
                cb.like(cb.lower(root.get("email")), pattern)
        );
    }
}
