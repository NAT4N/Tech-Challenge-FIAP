package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.port.out.RoleRepositoryPort;
import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.domain.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.UserResponseDTO;
import com.fiap.techchallenge14.domain.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
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
class UserUsecaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private RoleRepositoryPort roleRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserUsecaseImpl userUsecase;

    private User user;
    private UserResponseDTO userResponse;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setActive(true);

        userResponse = new UserResponseDTO(1L, "John", "email", "addr", "login", null, null, null, true, "CLIENT");

        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("CLIENT");
    }

    @Test
    void save_ShouldCreateUser() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO("John", "email", "pass", "addr", "login", 1L);
        when(userMapper.toDomain(dto)).thenReturn(user);
        when(roleRepository.findEntityById(1L)).thenReturn(Optional.of(roleEntity));
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        UserResponseDTO result = userUsecase.save(dto);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void save_ShouldThrowException_WhenRoleInvalid() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO("John", "email", "pass", "addr", "login", 1L);
        roleEntity.setName("INVALID");
        when(userMapper.toDomain(dto)).thenReturn(user);
        when(roleRepository.findEntityById(1L)).thenReturn(Optional.of(roleEntity));

        assertThrows(UserException.class, () -> userUsecase.save(dto));
    }

    @Test
    void update_ShouldUpdateUser() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("New", "new@email.com", "addr", "newlogin", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());
        when(userRepository.findByLogin("newlogin")).thenReturn(Optional.empty());
        when(roleRepository.findEntityById(1L)).thenReturn(Optional.of(roleEntity));
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        UserResponseDTO result = userUsecase.update(1L, dto);

        assertNotNull(result);
        verify(userMapper).updateDomainFromDto(dto, user);
    }

    @Test
    void update_ShouldThrowException_WhenEmailInUse() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("New", "inuse@email.com", "addr", "login", 1L);
        User other = new User();
        other.setId(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("inuse@email.com")).thenReturn(Optional.of(other));

        assertThrows(UserException.class, () -> userUsecase.update(1L, dto));
    }

    @Test
    void delete_ShouldDeactivateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userUsecase.delete(1L);

        assertFalse(user.getActive());
        verify(userRepository).save(user);
    }

    @Test
    void delete_ShouldDoNothing_WhenAlreadyInactive() {
        user.setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userUsecase.delete(1L);

        verify(userRepository, times(0)).save(any());
    }

    @Test
    void findUsers_ShouldReturnAll_WhenNameIsNull() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        List<UserResponseDTO> result = userUsecase.findUsers(null);

        assertEquals(1, result.size());
    }

    @Test
    void findUsers_ShouldFilterByName() {
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(List.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        List<UserResponseDTO> result = userUsecase.findUsers("John");

        assertEquals(1, result.size());
    }

    @Test
    void changePassword_ShouldUpdatePassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userUsecase.changePassword(1L, "newpass");

        assertEquals("newpass", user.getPassword());
        verify(userRepository).save(user);
    }
}
