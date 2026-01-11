package com.fiap.techchallenge14.application.usecase.login;

import com.fiap.techchallenge14.application.port.out.TokenMemoryPort;
import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.domain.dto.LoginRequestDTO;
import com.fiap.techchallenge14.domain.dto.LoginResponseDTO;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.exception.LoginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUsecaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private TokenMemoryPort tokenMemoryPort;

    @InjectMocks
    private LoginUsecaseImpl loginUsecase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setActive(true);
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        LoginRequestDTO request = new LoginRequestDTO("johndoe", "password");
        when(userRepository.findByLoginAndPassword("johndoe", "password")).thenReturn(Optional.of(user));

        LoginResponseDTO response = loginUsecase.login(request);

        assertNotNull(response);
        assertNotNull(response.token());
        verify(userRepository).save(user);
        verify(tokenMemoryPort).saveToken(eq(response.token()), eq(1L));
    }

    @Test
    void login_ShouldThrowException_WhenCredentialsAreInvalid() {
        LoginRequestDTO request = new LoginRequestDTO("johndoe", "wrong");
        when(userRepository.findByLoginAndPassword("johndoe", "wrong")).thenReturn(Optional.empty());

        assertThrows(LoginException.class, () -> loginUsecase.login(request));
    }

    @Test
    void login_ShouldThrowException_WhenUserIsInactive() {
        user.setActive(false);
        LoginRequestDTO request = new LoginRequestDTO("johndoe", "password");
        when(userRepository.findByLoginAndPassword("johndoe", "password")).thenReturn(Optional.of(user));

        assertThrows(LoginException.class, () -> loginUsecase.login(request));
    }
}
