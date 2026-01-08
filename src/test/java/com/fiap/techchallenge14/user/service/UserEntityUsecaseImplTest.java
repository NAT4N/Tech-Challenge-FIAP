package com.fiap.techchallenge14.user.service;

import com.fiap.techchallenge14.application.port.out.RoleRepositoryPort;
import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.application.usecase.user.UserUsecaseImpl;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.domain.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.UserResponseDTO;
import com.fiap.techchallenge14.domain.dto.UserUpdateRequestDTO;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityUsecaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private RoleRepositoryPort roleRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserUsecaseImpl userUseCaseImpl;

    private User user;
    private UserEntity userEntity;
    private RoleEntity role;

    @BeforeEach
    void setup() {
        role = new RoleEntity();
        role.setId(1L);
        role.setName(String.valueOf(RoleType.CLIENT));

        user = new User();
        user.setId(1L);
        user.setName("teste");
        user.setEmail("teste@email.com");
        user.setLogin("login");
        user.setActive(true);
        user.setRole(RoleType.CLIENT);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("teste");
        userEntity.setEmail("teste@email.com");
        userEntity.setLogin("login");
        userEntity.setActive(true);
        userEntity.setRole(role);
    }

    @Test
    void save_ShouldCreateUserSuccessfully() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO("teste", "teste@email.com", "123", "address", "login", 1L);

        when(userMapper.toDomain(dto)).thenReturn(user);
        when(roleRepository.findEntityById(1L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO(1L, "teste", "teste@email.com", "address", "login", null, null, null, true, RoleType.CLIENT.name()));

        UserResponseDTO result = userUseCaseImpl.save(dto);

        assertEquals("teste", result.name());
        assertEquals("teste@email.com", result.email());
        assertEquals(RoleType.CLIENT.name(), result.roleName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void update_ShouldUpdateUserSuccessfully() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("teste Updated", "updated@mail.com", "address", "login", 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findEntityById(1L)).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO(1L, "teste Updated", "updated@mail.com", "address", "login", null, null, null, true, RoleType.CLIENT.name()));

        UserResponseDTO result = userUseCaseImpl.update(1L, dto);

        assertNotNull(result);
        assertEquals("teste Updated", result.name());
        assertEquals("updated@mail.com", result.email());
        assertEquals("login", result.login());
    }

    @Test
    void update_ShouldThrowException_WhenUserNotFound() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("Test", "t@test.com", "address", "login", 1L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userUseCaseImpl.update(99L, dto));
    }

    @Test
    void update_ShouldThrowException_WhenEmailAlreadyInUse() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "teste Updated",
                "existing@email.com",
                "newLogin",
                "address",
                1L
        );

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail("existing@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(otherUser));

        UserException exception = assertThrows(UserException.class, () -> userUseCaseImpl.update(1L, dto));
        assertEquals("E-mail já está em uso", exception.getMessage());
    }

    @Test
    void update_ShouldThrowException_WhenLoginAlreadyInUse() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "teste Updated",
                "new@mail.com",
                "existingLogin",
                "address",
                1L
        );

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setLogin("existingLogin");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.of(otherUser));

        UserException exception = assertThrows(UserException.class, () -> userUseCaseImpl.update(1L, dto));
        assertEquals("Login já está em uso", exception.getMessage());
    }

    @Test
    void update_ShouldAllowSameEmailForSameUser() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "teste Updated",
                "teste@email.com",
                "newLogin",
                "address",
                1L
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.empty());
        when(roleRepository.findEntityById(dto.roleId())).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO(1L, "teste Updated", "teste@email.com", "address", "login", null, null, null, true, RoleType.CLIENT.name()));

        UserResponseDTO result = userUseCaseImpl.update(1L, dto);

        assertEquals("teste Updated", result.name());
        assertEquals("teste@email.com", result.email());
    }

    @Test
    void update_ShouldAllowSameLoginForSameUser() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "teste Updated",
                "new@mail.com",
                "address",
                "login",
                1L
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.of(user));
        when(roleRepository.findEntityById(dto.roleId())).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO(1L, "teste Updated", "new@mail.com", "address", "login", null, null, null, true, RoleType.CLIENT.name()));

        UserResponseDTO result = userUseCaseImpl.update(1L, dto);

        assertEquals("teste Updated", result.name());
        assertEquals("login", result.login());
    }

    @Test
    void delete_ShouldSoftDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userUseCaseImpl.delete(1L);

        assertFalse(user.getActive());
        verify(userRepository).save(user);
    }

    @Test
    void delete_ShouldNotSave_WhenAlreadyInactive() {
        user.setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userUseCaseImpl.delete(1L);

        verify(userRepository, never()).save(user);
    }

    @Test
    void delete_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userUseCaseImpl.delete(10L));
    }

    @Test
    void changePassword_ShouldUpdatePassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userUseCaseImpl.changePassword(1L, "newpass");

        assertEquals("newpass", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userUseCaseImpl.changePassword(50L, "newpass"));
    }

    @Test
    void shouldReturnUsers_whenNameIsProvided() {
        // given
        when(userRepository.findByNameContainingIgnoreCase("teste")).thenReturn(List.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO(1L, "teste", "teste@email.com", "address", "login", null, null, null, true, RoleType.CLIENT.name()));

        // when
        List<UserResponseDTO> result = userUseCaseImpl.findUsers("teste");

        // then
        assertEquals(1, result.size());
        UserResponseDTO dto = result.getFirst();
        assertEquals("teste", dto.name());
        assertEquals("teste@email.com", dto.email());
        assertEquals(RoleType.CLIENT.name(), dto.roleName());
    }

    @Test
    void shouldReturnEmptyList_whenRepositoryReturnsEmpty() {
        // given
        when(userRepository.findByNameContainingIgnoreCase("qualquer")).thenReturn(List.of());

        // when & then
        assertThrows(UserException.class, () -> userUseCaseImpl.findUsers("qualquer"));
    }

    @Test
    void shouldHandleNullNameParameter() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO(1L, "teste", "teste@email.com", "address", "login", null, null, null, true, RoleType.CLIENT.name()));

        // when
        List<UserResponseDTO> result = userUseCaseImpl.findUsers(null);

        // then
        assertEquals(1, result.size());
        assertEquals("teste", result.getFirst().name());
    }
}
