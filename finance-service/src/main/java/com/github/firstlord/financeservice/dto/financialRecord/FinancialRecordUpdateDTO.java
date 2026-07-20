package com.github.firstlord.financeservice.dto.financialRecord;

import com.github.firstlord.financeservice.enums.MeasureUnit;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FinancialRecordUpdateDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @Positive(message = "Quantity must be strictly positive")
    private BigDecimal quantity;

    private MeasureUnit measureUnit;

    @PositiveOrZero(message = "Unit price cannot be negative")
    private BigDecimal unitPrice;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    private UUID categoryId;
}
