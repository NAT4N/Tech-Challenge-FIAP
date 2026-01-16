package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.dto.RoleRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.exception.RoleException;
import com.fiap.techchallenge14.infrastructure.mapper.RoleEntityMapper;
import com.fiap.techchallenge14.infrastructure.mapper.RoleMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRoleUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private RoleEntityMapper roleEntityMapper;

    @InjectMocks
    private CreateRoleUseCase useCase;

    private RoleEntity savedEntity;
    private Role savedDomain;
    private RoleResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        savedEntity = new RoleEntity();
        savedEntity.setId(1L);
        savedEntity.setName("CLIENT");

        savedDomain = new Role();
        savedDomain.setId(1L);
        savedDomain.setName("CLIENT");

        responseDTO = new RoleResponseDTO(1L, "CLIENT");
    }

    @Test
    void execute_ShouldCreateRole_WhenNameDoesNotExist() {
        RoleRequestDTO dto = new RoleRequestDTO("  CLIENT  ");

        when(roleRepository.existsByName("CLIENT")).thenReturn(false);

        RoleEntity toSave = new RoleEntity();
        toSave.setName("CLIENT");

        when(roleEntityMapper.toEntity(any(Role.class))).thenReturn(toSave);
        when(roleRepository.save(toSave)).thenReturn(savedEntity);
        when(roleEntityMapper.toDomain(savedEntity)).thenReturn(savedDomain);
        when(roleMapper.toResponseDTO(savedDomain)).thenReturn(responseDTO);

        RoleResponseDTO result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("CLIENT", result.name());

        verify(roleRepository).existsByName("CLIENT");
        verify(roleEntityMapper).toEntity(any(Role.class));
        verify(roleRepository).save(toSave);
        verify(roleEntityMapper).toDomain(savedEntity);
        verify(roleMapper).toResponseDTO(savedDomain);
    }

    @Test
    void execute_ShouldThrowRoleException_WhenNameAlreadyExists() {
        RoleRequestDTO dto = new RoleRequestDTO("  CLIENT  ");

        when(roleRepository.existsByName("CLIENT")).thenReturn(true);

        RoleException ex = assertThrows(RoleException.class, () -> useCase.execute(dto));
        assertTrue(ex.getMessage().contains("Já existe um tipo de usuário com esse nome: CLIENT"));

        verify(roleRepository).existsByName("CLIENT");
        verify(roleRepository, never()).save(any());
        verifyNoInteractions(roleMapper);
        verifyNoInteractions(roleEntityMapper);
    }

    @Test
    void execute_ShouldCallExistsByName_WithTrimmedName() {
        RoleRequestDTO dto = new RoleRequestDTO("   ADMIN   ");
        when(roleRepository.existsByName("ADMIN")).thenReturn(true);

        assertThrows(RoleException.class, () -> useCase.execute(dto));

        verify(roleRepository).existsByName("ADMIN");
    }
}
