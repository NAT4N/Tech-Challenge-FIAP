package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.infrastructure.dto.RoleRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.exception.RoleException;
import com.fiap.techchallenge14.infrastructure.mapper.RoleMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateRoleUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public RoleResponseDTO execute(Long id, RoleRequestDTO dto) {

        RoleEntity entity = roleRepository.findById(id)
                .orElseThrow(() -> new RoleException("Tipo de usuário não encontrado"));

        String name = dto.name().trim();

        if (roleRepository.existsByNameAndIdNot(name, id)) {
            throw new RoleException("Já existe um tipo de usuário com esse nome: " + name);
        }

        entity.setName(name);

        RoleEntity saved = roleRepository.save(entity);
        return roleMapper.toResponseDTO(roleMapper.entityToDomain(saved));
    }
}
