package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserUpdateRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(source = "role", target = "roleName")
    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "role", ignore = true)
    User toDomain(UserCreateRequestDTO dto);

    @Mapping(target = "role", ignore = true)
    void updateDomainFromDto(UserUpdateRequestDTO dto, @MappingTarget User user);
}
