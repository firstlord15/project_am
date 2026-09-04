package com.github.firstlord.auth_service.mapper;

import com.github.firstlord.auth_service.dto.UserDTO;
import com.github.firstlord.auth_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserDTO toDTO(User user);
}
