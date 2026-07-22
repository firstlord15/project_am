package com.github.firstlord.financeservice.dto.category;

import com.github.firstlord.financeservice.enums.CategoryType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CategoryResponseDTO {
    private UUID id;
    private String title;
    private CategoryType type;
}
