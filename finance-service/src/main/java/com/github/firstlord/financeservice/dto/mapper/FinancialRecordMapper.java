package com.github.firstlord.financeservice.dto.mapper;

import com.github.firstlord.financeservice.dto.financialRecord.FinancialRecordDetailDTO;
import com.github.firstlord.financeservice.dto.financialRecord.FinancialRecordShortDTO;
import com.github.firstlord.financeservice.model.FinancialRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FinancialRecordMapper {

    @Mapping(target = "categoryTitle", source = "category.title")
    @Mapping(target = "categoryType", source = "category.type")
    FinancialRecordShortDTO toShortDTO(FinancialRecord record);

    FinancialRecordDetailDTO toDetailDTO(FinancialRecord record);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FinancialRecord fromCreateDTO(FinancialRecordShortDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FinancialRecord fromUpdateDTO(FinancialRecordShortDTO dto);
}
