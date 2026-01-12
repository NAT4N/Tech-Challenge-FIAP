package com.fiap.techchallenge14.application.adapters;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryAdapterTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepositoryAdapter adapter;

    @Test
    void findById_ShouldReturnRole() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1L);
        entity.setName("CLIENT");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Role> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findEntityById_ShouldReturnEntity() {
        RoleEntity entity = new RoleEntity();
        when(roleRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<RoleEntity> result = adapter.findEntityById(1L);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }
}
