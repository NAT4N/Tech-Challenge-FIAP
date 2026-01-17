package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.exception.RoleException;
import com.fiap.techchallenge14.infrastructure.mapper.RoleMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindRoleByIdUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public RoleResponseDTO execute(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDomain)
                .map(roleMapper::toResponseDTO)
                .orElseThrow(() -> new RoleException("Tipo de usuário não encontrado com o ID: " + id));
    }
}
