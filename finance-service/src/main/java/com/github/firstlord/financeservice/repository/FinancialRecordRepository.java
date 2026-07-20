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
     * Считаем стату по категориям для конкретного user и за выбранный период!
     */
    @Query("""
        SELECT
            c.id AS categoryId,
            c.name AS categoryName,
            c.type AS categoryType,
            COUNT(r.id) AS recordsCount,
            SUM(r.totalAmount) AS totalAmount
        FROM FinancialRecord r
        JOIN r.category c
        WHERE r.userId = :userId
        AND r.createdAt BETWEEN :startDate AND :endDate
        GROUP BY c.id, c.name, c.type
    """)
    List<CategoryStatsDTO> getCategoryStats(
            @Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    Optional<FinancialRecord> findByTitle(String title);
    List<FinancialRecord> findByCategory_IdAndUserId(UUID categoryId);
}
