package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserUpdateRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);

    User toDomain(UserCreateRequestDTO dto);

    void updateDomainFromDto(UserUpdateRequestDTO dto, @MappingTarget User user);
}
