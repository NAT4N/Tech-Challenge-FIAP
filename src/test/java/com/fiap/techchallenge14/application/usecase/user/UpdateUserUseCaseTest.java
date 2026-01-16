package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.support.UserPersistence;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
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
class UpdateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserPersistence userPersistence;

    @InjectMocks
    private UpdateUserUseCase useCase;

    private User user;
    private User updated;
    private UserResponseDTO response;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("old@email.com");
        user.setLogin("oldlogin");
        user.setActive(true);

        updated = new User();
        updated.setId(1L);
        updated.setName("New Name");
        updated.setEmail("new@email.com");
        updated.setLogin("newlogin");
        updated.setActive(true);

        response = new UserResponseDTO(
                1L, "New Name", "new@email.com", "addr", "newlogin",
                null, null, null, true, 1L
        );
    }

    @Test
    void execute_ShouldUpdateUser_WhenEmailAndLoginAreAvailable() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("New Name", "new@email.com", "addr", "newlogin", 1L);

        when(userPersistence.findDomainByIdOrThrow(1L)).thenReturn(user);
        when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());
        when(userRepository.findByLogin("newlogin")).thenReturn(Optional.empty());

        when(userPersistence.saveDomain(user)).thenReturn(updated);
        when(userMapper.toResponseDTO(updated)).thenReturn(response);

        UserResponseDTO result = useCase.execute(1L, dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("New Name", result.name());
        assertEquals("new@email.com", result.email());

        verify(userPersistence).findDomainByIdOrThrow(1L);
        verify(userRepository).findByEmail("new@email.com");
        verify(userRepository).findByLogin("newlogin");
        verify(userMapper).updateDomainFromDto(dto, user);
        verify(userPersistence).saveDomain(user);
        verify(userMapper).toResponseDTO(updated);
    }

    @Test
    void execute_ShouldAllowSameEmail_WhenBelongsToSameUser() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("New Name", "same@email.com", "addr", "newlogin", 1L);

        User sameUserEntity = new User();
        sameUserEntity.setId(1L);

        when(userPersistence.findDomainByIdOrThrow(1L)).thenReturn(user);
        when(userRepository.findByEmail("same@email.com")).thenReturn(Optional.empty());
        when(userRepository.findByLogin("newlogin")).thenReturn(Optional.empty());
        when(userPersistence.saveDomain(user)).thenReturn(updated);
        when(userMapper.toResponseDTO(updated)).thenReturn(response);

        assertDoesNotThrow(() -> useCase.execute(1L, dto));

        verify(userRepository).findByEmail("same@email.com");
        verify(userMapper).updateDomainFromDto(dto, user);
        verify(userPersistence).saveDomain(user);
    }

    @Test
    void execute_ShouldAllowSameLogin_WhenBelongsToSameUser() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("New Name", "new@email.com", "addr", "sameLogin", 1L);

        User sameUserEntity = new User();
        sameUserEntity.setId(1L);

        when(userPersistence.findDomainByIdOrThrow(1L)).thenReturn(user);
        when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());
        when(userRepository.findByLogin("sameLogin")).thenReturn(Optional.empty());
        when(userPersistence.saveDomain(user)).thenReturn(updated);
        when(userMapper.toResponseDTO(updated)).thenReturn(response);

        assertDoesNotThrow(() -> useCase.execute(1L, dto));

        verify(userRepository).findByLogin("sameLogin");
        verify(userMapper).updateDomainFromDto(dto, user);
        verify(userPersistence).saveDomain(user);
    }

    @Test
    void execute_ShouldThrowException_WhenEmailAlreadyInUseByAnotherUser() {
        UserUpdateRequestDTO dto =
                new UserUpdateRequestDTO("New Name", "inuse@email.com", "addr", "newlogin", 1L);

        UserEntity other = new UserEntity();
        other.setId(2L);

        when(userPersistence.findDomainByIdOrThrow(1L)).thenReturn(user);

        when(userRepository.findByEmail("inuse@email.com")).thenReturn(Optional.of(other));

        UserException ex = assertThrows(UserException.class, () -> useCase.execute(1L, dto));
        assertEquals("E-mail já está em uso", ex.getMessage());

        verify(userRepository).findByEmail("inuse@email.com");
        verify(userRepository, never()).findByLogin(anyString());
        verify(userMapper, never()).updateDomainFromDto(any(), any());
        verify(userPersistence, never()).saveDomain(any());
    }

    @Test
    void execute_ShouldThrowException_WhenLoginAlreadyInUseByAnotherUser() {
        UserUpdateRequestDTO dto =
                new UserUpdateRequestDTO("New Name", "new@email.com", "addr", "inuseLogin", 1L);

        UserEntity other = new UserEntity();
        other.setId(2L);

        when(userPersistence.findDomainByIdOrThrow(1L)).thenReturn(user);

        when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());
        when(userRepository.findByLogin("inuseLogin")).thenReturn(Optional.of(other));

        UserException ex = assertThrows(UserException.class, () -> useCase.execute(1L, dto));
        assertEquals("Login já está em uso", ex.getMessage());

        verify(userRepository).findByEmail("new@email.com");
        verify(userRepository).findByLogin("inuseLogin");
        verify(userMapper, never()).updateDomainFromDto(any(), any());
        verify(userPersistence, never()).saveDomain(any());
    }

    @Test
    void execute_ShouldPropagateException_WhenUserNotFound() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("New Name", "new@email.com", "addr", "newlogin", 1L);

        when(userPersistence.findDomainByIdOrThrow(1L))
                .thenThrow(new RuntimeException("user not found"));

        assertThrows(RuntimeException.class, () -> useCase.execute(1L, dto));

        verify(userPersistence).findDomainByIdOrThrow(1L);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(userMapper);
        verify(userPersistence, never()).saveDomain(any());
    }
}
