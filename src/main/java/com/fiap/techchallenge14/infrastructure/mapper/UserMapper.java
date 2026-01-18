package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);

    User toDomain(UserCreateRequestDTO dto);

    void updateDomainFromDto(UserUpdateRequestDTO dto, @MappingTarget User user);

    @Mapping(source = "role.id", target = "roleId")
    User entityToDomain(UserEntity entity);

    @Mapping(target = "role", ignore = true)
    void updateEntityFromDomain(User domain, @MappingTarget UserEntity entity);
}
