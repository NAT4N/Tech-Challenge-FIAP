package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.dto.RoleRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.mapper.RoleEntityMapper;
import com.fiap.techchallenge14.infrastructure.mapper.RoleMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void execute_ShouldCreateRole_TrimmingName() {
        RoleRequestDTO dto = new RoleRequestDTO("  CLIENT  ");

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
        verify(roleRepository).save(toSave);
        verify(roleEntityMapper).toDomain(savedEntity);
        verify(roleMapper).toResponseDTO(savedDomain);
    }
}
