package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserEntityMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "roleEntityToRoleType")
    User toDomain(UserEntity entity);

    @Mapping(target = "role", ignore = true)
    UserEntity toEntity(User domain);

    @Mapping(target = "role", ignore = true)
    void updateEntityFromDomain(User domain, @MappingTarget UserEntity entity);

    @Named("roleEntityToRoleType")
    default RoleType roleEntityToRoleType(RoleEntity roleEntity) {
        if (roleEntity == null || roleEntity.getName() == null) {
            return null;
        }
        try {
            return RoleType.valueOf(roleEntity.getName());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
