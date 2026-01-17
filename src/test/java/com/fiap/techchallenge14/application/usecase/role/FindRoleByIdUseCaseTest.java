package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.domain.model.Role;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindRoleByIdUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private FindRoleByIdUseCase useCase;

    private RoleEntity roleEntity;
    private Role roleDomain;
    private RoleResponseDTO roleResponse;

    @BeforeEach
    void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("CLIENT");

        roleDomain = new Role();
        roleDomain.setId(1L);
        roleDomain.setName("CLIENT");

        roleResponse = new RoleResponseDTO(1L, "CLIENT");
    }

    @Test
    void execute_ShouldReturnRole_WhenFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));
        when(roleMapper.entityToDomain(roleEntity)).thenReturn(roleDomain);
        when(roleMapper.toResponseDTO(roleDomain)).thenReturn(roleResponse);

        RoleResponseDTO result = useCase.execute(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("CLIENT", result.name());

        verify(roleRepository).findById(1L);
        verify(roleMapper).entityToDomain(roleEntity);
        verify(roleMapper).toResponseDTO(roleDomain);
    }

    @Test
    void execute_ShouldThrowRoleException_WhenNotFound() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        RoleException ex = assertThrows(RoleException.class, () -> useCase.execute(99L));
        assertTrue(ex.getMessage().contains("Tipo de usuário não encontrado com o ID: 99"));

        verify(roleRepository).findById(99L);
        verifyNoInteractions(roleMapper);
        verifyNoInteractions(roleMapper);
    }
}
