package com.github.firstlord.financeservice.model;

import com.github.firstlord.financeservice.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Entity
@RequiredArgsConstructor
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
    @Column(nullable = false)
    private String userId;

    /**
     * Название категории.
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * Описание категории.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Тип категории.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType categoryType;
}
