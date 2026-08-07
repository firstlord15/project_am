package com.github.firstlord.financeservice.model;

import com.github.firstlord.financeservice.enums.CategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "categories",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_category_user_title",
                columnNames = {"user_id", "title"}
        )
)
public class Category {

    /**
     * Уникальный идентификатор категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Идентификатор пользователя, создателя записи.
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * Название категории.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Тип категории.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type;
}
