package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.dto.RoleRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.exception.RoleException;
import com.fiap.techchallenge14.infrastructure.mapper.RoleEntityMapper;
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
    private final RoleEntityMapper roleEntityMapper;

    @Transactional
    public RoleResponseDTO execute(RoleRequestDTO dto) {
        String name = dto.name() != null ? dto.name().trim() : null;
        if (name == null || name.isBlank()) {
            throw new RoleException("O nome do tipo é obrigatório");
        }

        // normaliza via domain/type quando possível
        Role domain = roleMapper.toDomain(new RoleRequestDTO(name));
        RoleEntity entity = roleEntityMapper.toEntity(domain);

        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new RoleException("Tipo de usuário inválido: " + name);
        }

        if (roleRepository.existsByName(entity.getName())) {
            throw new RoleException("Já existe um tipo de usuário com esse nome: " + entity.getName());
        }

        RoleEntity saved = roleRepository.save(entity);
        log.info("Tipo de usuário criado com ID: {}", saved.getId());

        Role savedDomain = roleEntityMapper.toDomain(saved);
        return roleMapper.toResponseDTO(savedDomain);
    }
}
