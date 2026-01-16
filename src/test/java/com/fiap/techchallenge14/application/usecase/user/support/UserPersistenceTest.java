package com.fiap.techchallenge14.application.usecase.user.support;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.infrastructure.mapper.UserEntityMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserPersistenceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @InjectMocks
    private UserPersistence userPersistence;

    private UserEntity entity;
    private User domain;
    private RoleEntity role;

    @BeforeEach
    void setUp() {
        entity = new UserEntity();
        entity.setId(1L);

        domain = new User();
        domain.setId(1L);
        domain.setName("John");
        domain.setRoleId(10L);

        role = new RoleEntity();
        role.setId(10L);
        role.setName("CLIENT");
    }

    @Test
    void findDomainByIdOrThrow_ShouldReturnDomain_WhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userEntityMapper.toDomain(entity)).thenReturn(domain);

        User result = userPersistence.findDomainByIdOrThrow(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findById(1L);
        verify(userEntityMapper).toDomain(entity);
    }

    @Test
    void findDomainByIdOrThrow_ShouldThrowUserException_WhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserException ex = assertThrows(UserException.class, () -> userPersistence.findDomainByIdOrThrow(1L));
        assertTrue(ex.getMessage().contains("Usuário não encontrado com o ID: 1"));

        verify(userRepository).findById(1L);
        verifyNoInteractions(userEntityMapper);
    }

    @Test
    void saveDomain_ShouldCreateNewEntity_WhenUserIdIsNull() {
        User newUser = new User();
        newUser.setId(null);
        newUser.setName("John");
        newUser.setRoleId(10L);

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(99L);

        User savedDomain = new User();
        savedDomain.setId(99L);
        savedDomain.setRoleId(10L);

        when(roleRepository.findById(10L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
        when(userEntityMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        User result = userPersistence.saveDomain(newUser);

        assertNotNull(result);
        assertEquals(99L, result.getId());

        verify(userRepository, never()).findById(anyLong());

        verify(userEntityMapper).updateEntityFromDomain(eq(newUser), any(UserEntity.class));

        verify(roleRepository).findById(10L);

        verify(userRepository).save(any(UserEntity.class));
        verify(userEntityMapper).toDomain(savedEntity);
    }

    @Test
    void saveDomain_ShouldUpdateExistingEntity_WhenUserExists() {
        UserEntity existingEntity = new UserEntity();
        existingEntity.setId(1L);

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(roleRepository.findById(10L)).thenReturn(Optional.of(role));
        when(userRepository.save(existingEntity)).thenReturn(savedEntity);
        when(userEntityMapper.toDomain(savedEntity)).thenReturn(domain);

        User result = userPersistence.saveDomain(domain);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(userRepository).findById(1L);
        verify(userEntityMapper).updateEntityFromDomain(domain, existingEntity);
        verify(roleRepository).findById(10L);

        verify(userRepository).save(existingEntity);
        verify(userEntityMapper).toDomain(savedEntity);
    }

    @Test
    void saveDomain_ShouldCreateNewEntity_WhenUserIdNotNullButEntityNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(roleRepository.findById(10L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(domain);

        User result = userPersistence.saveDomain(domain);

        assertNotNull(result);

        verify(userRepository).findById(1L);
        verify(userEntityMapper).updateEntityFromDomain(eq(domain), any(UserEntity.class));
        verify(roleRepository).findById(10L);
        verify(userRepository).save(any(UserEntity.class));
        verify(userEntityMapper).toDomain(any(UserEntity.class));
    }

    @Test
    void saveDomain_ShouldThrowUserException_WhenRoleNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(roleRepository.findById(10L)).thenReturn(Optional.empty());

        UserException ex = assertThrows(UserException.class, () -> userPersistence.saveDomain(domain));
        assertTrue(ex.getMessage().contains("Perfil não encontrado com o ID: 10"));

        verify(userRepository).findById(1L);
        verify(userEntityMapper).updateEntityFromDomain(domain, entity);
        verify(roleRepository).findById(10L);

        verify(userRepository, never()).save(any());
        verify(userEntityMapper, never()).toDomain(any());
    }
}
