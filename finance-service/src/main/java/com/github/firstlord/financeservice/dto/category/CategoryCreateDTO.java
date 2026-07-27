package com.github.firstlord.financeservice.dto.category;

import com.github.firstlord.financeservice.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDTO {
    @NotBlank(message = "Category name is required")
    @Size(max = 255)
    private String title;

    @NotNull(message = "Category type is required")
    private CategoryType type;
}
