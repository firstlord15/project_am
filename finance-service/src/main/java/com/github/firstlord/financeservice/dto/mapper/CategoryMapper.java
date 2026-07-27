package com.github.firstlord.financeservice.dto.mapper;

import com.github.firstlord.financeservice.dto.category.CategoryCreateDTO;
import com.github.firstlord.financeservice.dto.category.CategoryResponseDTO;
import com.github.firstlord.financeservice.dto.category.CategoryUpdateDTO;
import com.github.firstlord.financeservice.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryResponseDTO toDTO(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Category fromCreateDTO(CategoryCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Category fromUpdateDTO(CategoryUpdateDTO updateDTO);
}
