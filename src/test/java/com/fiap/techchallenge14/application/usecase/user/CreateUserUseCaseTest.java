package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.support.UserPersistence;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserPersistence userPersistence;

    @InjectMocks
    private CreateUserUseCase useCase;

    private User user;
    private User saved;
    private UserResponseDTO response;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("John");
        user.setActive(false);

        saved = new User();
        saved.setId(1L);
        saved.setName("John");
        saved.setActive(true);

        response = new UserResponseDTO(
                1L, "John", "email", "addr", "login",
                null, null, null, true, 1L
        );
    }

    @Test
    void execute_ShouldCreateUser_AndSetAuditFields() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO("John", "email", "pass", "addr", "login", 1L);

        when(userMapper.toDomain(dto)).thenReturn(user);

        when(userPersistence.saveDomain(any(User.class))).thenReturn(saved);

        when(userMapper.toResponseDTO(saved)).thenReturn(response);

        UserResponseDTO result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertTrue(user.getActive(), "usecase deve setar active=true");

        assertNotNull(user.getCreatedAt(), "usecase deve setar createdAt");
        assertNotNull(user.getLastUpdatedAt(), "usecase deve setar lastUpdatedAt");

        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(user.getLastUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));

        verify(userMapper).toDomain(dto);
        verify(userPersistence).saveDomain(user);
        verify(userMapper).toResponseDTO(saved);
    }

    @Test
    void execute_ShouldPropagateException_WhenPersistenceFails() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO("John", "email", "pass", "addr", "login", 1L);

        when(userMapper.toDomain(dto)).thenReturn(user);
        when(userPersistence.saveDomain(any(User.class))).thenThrow(new RuntimeException("db down"));

        assertThrows(RuntimeException.class, () -> useCase.execute(dto));

        verify(userMapper).toDomain(dto);
        verify(userPersistence).saveDomain(user);
        verify(userMapper, never()).toResponseDTO(any());
    }
}
