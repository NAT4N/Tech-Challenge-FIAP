package com.fiap.techchallenge14.application.port.out;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;

import java.util.Optional;

public interface RoleRepositoryPort {
    Optional<Role> findById(Long id);

    Optional<RoleEntity> findEntityById(Long id);
}
