package com.github.firstlord.financeservice.dto.category;

import com.github.firstlord.financeservice.enums.CategoryType;

import java.math.BigDecimal;
import java.util.UUID;

public interface CategoryStatsDTO {
    UUID getCategoryId();
    String getCategoryTitle();
    CategoryType getCategoryType();
    Long getRecordsCount();
    BigDecimal getTotalAmount();
}
