package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.mapper.RoleEntityMapper;
import com.fiap.techchallenge14.infrastructure.mapper.RoleMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllRolesUseCase {

    private final RoleRepository roleRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> execute() {
        return roleRepository.findAll().stream()
                .map(roleEntityMapper::toDomain)
                .map(roleMapper::toResponseDTO)
                .toList();
    }
}
