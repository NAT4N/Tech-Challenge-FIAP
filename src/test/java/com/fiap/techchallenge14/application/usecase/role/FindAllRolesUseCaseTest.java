package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.mapper.RoleMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllRolesUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private FindAllRolesUseCase useCase;

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
    void execute_ShouldReturnAllRolesMapped_WhenRolesExist() {
        when(roleRepository.findAll()).thenReturn(List.of(roleEntity));
        when(roleMapper.entityToDomain(roleEntity)).thenReturn(roleDomain);
        when(roleMapper.toResponseDTO(roleDomain)).thenReturn(roleResponse);

        List<RoleResponseDTO> result = useCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals("CLIENT", result.getFirst().name());

        verify(roleRepository).findAll();
        verify(roleMapper).entityToDomain(roleEntity);
        verify(roleMapper).toResponseDTO(roleDomain);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenNoRolesExist() {
        when(roleRepository.findAll()).thenReturn(List.of());

        List<RoleResponseDTO> result = useCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(roleRepository).findAll();
        verifyNoInteractions(roleMapper);
    }
}
