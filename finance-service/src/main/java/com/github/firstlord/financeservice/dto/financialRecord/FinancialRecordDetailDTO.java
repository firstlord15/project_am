package com.github.firstlord.financeservice.dto.financialRecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.firstlord.financeservice.dto.category.CategoryResponseDTO;
import com.github.firstlord.financeservice.enums.MeasureUnit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FinancialRecordDetailDTO {
    private UUID id;
    private String title;
    private BigDecimal quantity;
    private MeasureUnit measureUnit;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String comment;
    private CategoryResponseDTO category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
