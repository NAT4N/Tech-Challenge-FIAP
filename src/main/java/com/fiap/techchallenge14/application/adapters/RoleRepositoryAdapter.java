package com.fiap.techchallenge14.application.adapters;

import com.fiap.techchallenge14.application.port.out.RoleRepositoryPort;
import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import com.fiap.techchallenge14.infrastructure.mapper.RoleEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RoleRepository roleRepository;

    public RoleRepositoryAdapter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id).map(RoleEntityMapper::toDomain);
    }

    @Override
    public Optional<RoleEntity> findEntityById(Long id) {
        return roleRepository.findById(id);
    }
}
