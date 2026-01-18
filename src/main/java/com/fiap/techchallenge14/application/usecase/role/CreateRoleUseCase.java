package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.dto.RoleRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.mapper.RoleMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public RoleResponseDTO execute(RoleRequestDTO dto) {
        String name = dto.name().trim();

        Role domain = new Role();
        domain.setName(name);

        RoleEntity entity = roleMapper.toEntity(domain);
        RoleEntity saved = roleRepository.save(entity);

        return roleMapper.toResponseDTO(roleMapper.entityToDomain(saved));
    }
}
