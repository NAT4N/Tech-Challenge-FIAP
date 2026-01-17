package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.dto.RoleRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.exception.RoleException;
import com.fiap.techchallenge14.infrastructure.mapper.RoleMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRoleUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UpdateRoleUseCase useCase;

    private RoleEntity existingEntity;
    private Role domain;
    private RoleResponseDTO response;

    @BeforeEach
    void setUp() {
        existingEntity = new RoleEntity();
        existingEntity.setId(1L);
        existingEntity.setName("OLD");

        domain = new Role();
        domain.setId(1L);
        domain.setName("NEW");

        response = new RoleResponseDTO(1L, "NEW");
    }

    @Test
    void execute_ShouldUpdateRole_WhenNameIsAvailable() {
        RoleRequestDTO dto = new RoleRequestDTO("  NEW  ");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(roleRepository.existsByNameAndIdNot("NEW", 1L)).thenReturn(false);
        when(roleRepository.save(existingEntity)).thenReturn(existingEntity);
        when(roleMapper.entityToDomain(existingEntity)).thenReturn(domain);
        when(roleMapper.toResponseDTO(domain)).thenReturn(response);

        RoleResponseDTO result = useCase.execute(1L, dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("NEW", result.name());
        assertEquals("NEW", existingEntity.getName(), "nome deve ser atualizado na entity");

        verify(roleRepository).findById(1L);
        verify(roleRepository).existsByNameAndIdNot("NEW", 1L);
        verify(roleRepository).save(existingEntity);
        verify(roleMapper).entityToDomain(existingEntity);
        verify(roleMapper).toResponseDTO(domain);
    }

    @Test
    void execute_ShouldThrowRoleException_WhenRoleNotFound() {
        RoleRequestDTO dto = new RoleRequestDTO("NEW");

        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        RoleException ex = assertThrows(RoleException.class, () -> useCase.execute(1L, dto));
        assertEquals("Tipo de usuário não encontrado", ex.getMessage());

        verify(roleRepository).findById(1L);
        verify(roleRepository, never()).save(any());
        verifyNoInteractions(roleMapper);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void execute_ShouldThrowRoleException_WhenNameAlreadyExistsForAnotherRole() {
        RoleRequestDTO dto = new RoleRequestDTO("  ADMIN  ");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(roleRepository.existsByNameAndIdNot("ADMIN", 1L)).thenReturn(true);

        RoleException ex = assertThrows(RoleException.class, () -> useCase.execute(1L, dto));
        assertEquals("Já existe um tipo de usuário com esse nome: ADMIN", ex.getMessage());

        verify(roleRepository).findById(1L);
        verify(roleRepository).existsByNameAndIdNot("ADMIN", 1L);
        verify(roleRepository, never()).save(any());
        verifyNoInteractions(roleMapper);
        verifyNoInteractions(roleMapper);
    }
}
