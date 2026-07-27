package com.github.firstlord.financeservice.repository;

import com.github.firstlord.financeservice.dto.category.CategoryStatsDTO;
import com.github.firstlord.financeservice.model.FinancialRecord;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, UUID> {
    /**
     * Агрегирует статистику финансовых записей по категориям для конкретного пользователя
     * за указанный период времени.
     * <p>
     * Выполняет группировку (GROUP BY) по категориям, подсчитывая количество записей
     * и суммируя их общую стоимость.
     *
     * @param userId    идентификатор пользователя, чьи данные необходимо получить
     * @param startDate дата и время начала периода (включительно)
     * @param endDate   дата и время окончания периода (включительно)
     * @return список объектов {@link CategoryStatsDTO} со статистикой по каждой найденной категории.
     *         Если за выбранный период записей нет, вернется пустой список.
     */
    @Query("""
        SELECT
            c.id AS categoryId,
            c.title AS categoryTitle,
            c.type AS categoryType,
            COUNT(r.id) AS recordsCount,
            SUM(r.totalAmount) AS totalAmount
        FROM FinancialRecord r
        JOIN r.category c
        WHERE r.userId = :userId
        AND r.createdAt BETWEEN :startDate AND :endDate
        GROUP BY c.id, c.title, c.type
    """)
    List<CategoryStatsDTO> getCategoryStats(
            @Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    Optional<FinancialRecord> findByTitle(String title);
    List<FinancialRecord> findByCategory_IdAndUserId(UUID category_id, String userId);
}
