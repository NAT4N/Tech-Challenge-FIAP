package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.domain.model.RoleType;

public class RoleEntityMapper {

    public static Role toDomain(RoleEntity entity) {
        if (entity == null) return null;
        Role domain = new Role();
        domain.setId(entity.getId());

        String name = entity.getName() != null ? entity.getName() : null;
        domain.setName(name);

        if (entity.getName() != null) {
            try {
                domain.setType(String.valueOf(RoleType.valueOf(entity.getName())));
            } catch (Exception ignored) {}
        }
        return domain;
    }

    public static RoleEntity toEntity(Role domain) {
        if (domain == null) return null;
        RoleEntity entity = new RoleEntity();
        entity.setId(domain.getId());
        if (domain.getType() != null) {
            try {
                entity.setName(String.valueOf(RoleType.valueOf(domain.getType())));
            } catch (Exception ignored) {}
        } else if (domain.getName() != null) {
            try {
                entity.setName(String.valueOf(RoleType.valueOf(domain.getName())));
            } catch (Exception ignored) {}
        }
        return entity;
    }
}
