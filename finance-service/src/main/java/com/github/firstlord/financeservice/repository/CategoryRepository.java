package com.github.firstlord.financeservice.repository;

import com.github.firstlord.financeservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByTitle(String title);
    List<Category> findAllByUserId(String userId);
}
