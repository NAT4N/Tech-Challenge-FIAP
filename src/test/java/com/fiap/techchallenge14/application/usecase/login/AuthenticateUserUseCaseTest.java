package com.fiap.techchallenge14.application.usecase.login;

import com.fiap.techchallenge14.infrastructure.dto.LoginRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.LoginResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.LoginException;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import com.fiap.techchallenge14.infrastructure.security.InMemoryToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InMemoryToken inMemoryToken;

    @InjectMocks
    private AuthenticateUserUseCase authenticateUserUseCase;

    private UserEntity activeUser;

    @BeforeEach
    void setUp() {
        activeUser = new UserEntity();
        activeUser.setId(1L);
        activeUser.setName("John");
        activeUser.setActive(true);
    }

    @Test
    void execute_ShouldAuthenticate_UpdateLastLogin_SaveUser_AndReturnToken() {
        LoginRequestDTO dto = new LoginRequestDTO("john", "pass");

        when(userRepository.findByLoginAndPassword("john", "pass"))
                .thenReturn(Optional.of(activeUser));

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        LocalDateTime before = LocalDateTime.now();

        LoginResponseDTO response = authenticateUserUseCase.execute(dto);

        LocalDateTime after = LocalDateTime.now();

        assertNotNull(response);
        assertNotNull(response.token());
        assertFalse(response.token().isBlank());

        assertNotNull(activeUser.getLastLoginAt(), "deve setar lastLoginAt");
        assertTrue(!activeUser.getLastLoginAt().isBefore(before) && !activeUser.getLastLoginAt().isAfter(after),
                "lastLoginAt deve estar entre before e after");

        verify(userRepository).findByLoginAndPassword("john", "pass");
        verify(userRepository).save(activeUser);

        verify(inMemoryToken).saveToken(anyString(), eq(1L));
    }

    @Test
    void execute_ShouldThrowLoginException_WhenCredentialsInvalid() {
        LoginRequestDTO dto = new LoginRequestDTO("john", "wrong");

        when(userRepository.findByLoginAndPassword("john", "wrong"))
                .thenReturn(Optional.empty());

        LoginException ex = assertThrows(LoginException.class, () -> authenticateUserUseCase.execute(dto));
        assertEquals("Login ou senha inválidos", ex.getMessage());

        verify(userRepository).findByLoginAndPassword("john", "wrong");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(inMemoryToken);
    }

    @Test
    void execute_ShouldThrowLoginException_WhenUserIsInactive() {
        LoginRequestDTO dto = new LoginRequestDTO("john", "pass");

        UserEntity inactive = new UserEntity();
        inactive.setId(2L);
        inactive.setName("Inactive");
        inactive.setActive(false);

        when(userRepository.findByLoginAndPassword("john", "pass"))
                .thenReturn(Optional.of(inactive));

        LoginException ex = assertThrows(LoginException.class, () -> authenticateUserUseCase.execute(dto));
        assertEquals("Login ou senha inválidos", ex.getMessage());

        verify(userRepository).findByLoginAndPassword("john", "pass");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(inMemoryToken);
    }
}
