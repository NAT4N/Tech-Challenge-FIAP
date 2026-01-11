package com.fiap.techchallenge14.application.adapters;

import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.mapper.UserEntityMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserEntityMapper mapper;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setRole(RoleType.CLIENT);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
    }

    @Test
    void findById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        Optional<User> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        Optional<User> result = adapter.findByEmail("test@test.com");

        assertTrue(result.isPresent());
    }

    @Test
    void findByLogin_ShouldReturnUser() {
        when(userRepository.findByLogin("login")).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        Optional<User> result = adapter.findByLogin("login");

        assertTrue(result.isPresent());
    }

    @Test
    void findByLoginAndPassword_ShouldReturnUser() {
        when(userRepository.findByLoginAndPassword("login", "pass")).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        Optional<User> result = adapter.findByLoginAndPassword("login", "pass");

        assertTrue(result.isPresent());
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnList() {
        when(userRepository.findByNameContainingIgnoreCase("name")).thenReturn(List.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        List<User> result = adapter.findByNameContainingIgnoreCase("name");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findAll_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        List<User> result = adapter.findAll();

        assertFalse(result.isEmpty());
    }

    @Test
    void save_NewUser_ShouldSaveAndReturnUser() {
        user.setId(null);
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("CLIENT");

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(roleRepository.findByName("CLIENT")).thenReturn(Optional.of(roleEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = adapter.save(user);

        assertNotNull(result);
        verify(userRepository).save(any(UserEntity.class));
        verify(mapper).updateEntityFromDomain(eq(user), any(UserEntity.class));
    }

    @Test
    void save_ExistingUser_ShouldUpdateAndReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(roleRepository.findByName("CLIENT")).thenReturn(Optional.of(new RoleEntity()));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = adapter.save(user);

        assertNotNull(result);
        verify(userRepository).save(userEntity);
    }

    @Test
    void save_RoleNotFound_ShouldThrowException() {
        when(roleRepository.findByName("CLIENT")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> adapter.save(user));
    }

    @Test
    void update_ShouldSaveUserWithId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(roleRepository.findByName("CLIENT")).thenReturn(Optional.of(new RoleEntity()));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = adapter.update(1L, user);

        assertNotNull(result);
        assertEquals(1L, user.getId());
    }
}
