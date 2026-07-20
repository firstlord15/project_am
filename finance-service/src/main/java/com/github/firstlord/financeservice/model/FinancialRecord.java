package com.github.firstlord.financeservice.model;

import com.github.firstlord.financeservice.enums.MeasureUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "financial_records")
public class FinancialRecord {

    /**
     * Уникальный идентификатор финансовой записи.
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
     * Краткое название записи.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Количество или объем проданного/купленного.
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal quantity;

    /**
     * Единица измерения для поля quantity.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "measure_unit")
    private MeasureUnit measureUnit; // Единица измерения

    /**
     * Цена за одну единицу измерения.
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal unitPrice;

    /**
     * Итоговая сумма записи.
     * Если quantity и unitPrice есть, она равна их произведению.
     * Если их нет, пользователь ввел ее вручную.
     */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    /**
     * Дополнительный комментарий.
     */
    @Column(nullable = false)
    private String comment;

    /**
     * Категория транзакции.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Дата и время создания записи.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Дата и время последнего изменения записи.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Автоматическая инициализация данных перед сохранением новой записи в БД.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Автоматическая актуализация данных перед обновлением записи в БД.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
