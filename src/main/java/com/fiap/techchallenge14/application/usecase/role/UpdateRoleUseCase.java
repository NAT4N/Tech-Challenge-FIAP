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
public class UpdateRoleUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleEntityMapper roleEntityMapper;

    @Transactional
    public RoleResponseDTO execute(Long id, RoleRequestDTO dto) {
        RoleEntity entity = roleRepository.findById(id)
                .orElseThrow(() -> new RoleException("Tipo de usuário não encontrado com o ID: " + id));

        // Entity -> Domain, aplica update, Domain -> Entity (merge)
        Role domain = roleEntityMapper.toDomain(entity);

        // normaliza o input (trim) antes do mapper
        RoleRequestDTO normalized = new RoleRequestDTO(dto.name() != null ? dto.name().trim() : null);
        if (normalized.name() == null || normalized.name().isBlank()) {
            throw new RoleException("O nome do tipo é obrigatório");
        }

        roleMapper.updateDomainFromDto(normalized, domain);

        // aplica no entity via mapper entity
        roleEntityMapper.updateEntityFromDomain(domain, entity);

        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new RoleException("Tipo de usuário inválido: " + normalized.name());
        }

        if (roleRepository.existsByNameAndIdNot(entity.getName(), id)) {
            throw new RoleException("Já existe um tipo de usuário com esse nome: " + entity.getName());
        }

        RoleEntity saved = roleRepository.save(entity);
        log.info("Tipo de usuário atualizado com ID: {}", saved.getId());

        return roleMapper.toResponseDTO(roleEntityMapper.toDomain(saved));
    }
}
