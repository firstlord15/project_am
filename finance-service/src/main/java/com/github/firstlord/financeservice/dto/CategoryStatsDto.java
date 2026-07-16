package com.github.firstlord.financeservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public interface CategoryStatsDto {
    UUID getCategoryId();
    String getCategoryName();
    Long getRecordsCount();
    BigDecimal getTotalAmount();
}
