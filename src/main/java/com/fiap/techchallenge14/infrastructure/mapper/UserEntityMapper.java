package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserEntityMapper {

    @Mapping(source = "role.id", target = "roleId")
    User toDomain(UserEntity entity);

    @Mapping(target = "role", ignore = true)
    UserEntity toEntity(User domain);

    @Mapping(target = "role", ignore = true)
    void updateEntityFromDomain(User domain, @MappingTarget UserEntity entity);
}
