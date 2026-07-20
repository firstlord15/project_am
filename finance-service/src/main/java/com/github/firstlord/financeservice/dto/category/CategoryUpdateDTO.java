package com.github.firstlord.financeservice.dto.category;

import com.github.firstlord.financeservice.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryUpdateDTO {
    @NotBlank(message = "Category name is required")
    @Size(max = 255)
    private String name;

    @NotNull(message = "Category type is required")
    private CategoryType type;
}
