package com.github.firstlord.financeservice.dto.financialRecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.firstlord.financeservice.enums.CategoryType;
import com.github.firstlord.financeservice.enums.MeasureUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordShortDTO {
    private UUID id;
    private String title;
    private BigDecimal quantity;
    private MeasureUnit measureUnit;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String categoryName;
    private CategoryType categoryType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
