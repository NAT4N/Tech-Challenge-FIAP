package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUsersUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private FindUsersUseCase useCase;

    private UserEntity userEntity;
    private User userDomain;
    private UserResponseDTO userResponse;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("John");

        userDomain = new User();
        userDomain.setId(1L);
        userDomain.setName("John");

        userResponse = new UserResponseDTO(
                1L, "John", "email", "addr", "login",
                null, null, null, true, 1L
        );
    }

    @Test
    void execute_ShouldReturnAll_WhenNameIsNull() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(userDomain);
        when(userMapper.toResponseDTO(userDomain)).thenReturn(userResponse);

        List<UserResponseDTO> result = useCase.execute(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());

        verify(userRepository).findAll();
        verify(userRepository, never()).findByNameContainingIgnoreCase(anyString());
        verify(userMapper).toDomain(userEntity);
        verify(userMapper).toResponseDTO(userDomain);
    }

    @Test
    void execute_ShouldReturnAll_WhenNameIsBlank() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(userDomain);
        when(userMapper.toResponseDTO(userDomain)).thenReturn(userResponse);

        List<UserResponseDTO> result = useCase.execute("   ");

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(userRepository).findAll();
        verify(userRepository, never()).findByNameContainingIgnoreCase(anyString());
    }

    @Test
    void execute_ShouldFilterByName_WhenNameProvided() {
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(List.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(userDomain);
        when(userMapper.toResponseDTO(userDomain)).thenReturn(userResponse);

        List<UserResponseDTO> result = useCase.execute("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().name());

        verify(userRepository, never()).findAll();
        verify(userRepository).findByNameContainingIgnoreCase("John");
        verify(userMapper).toDomain(userEntity);
        verify(userMapper).toResponseDTO(userDomain);
    }

    @Test
    void execute_ShouldThrowUserException_WhenNoUsersFound_WithNullName() {
        when(userRepository.findAll()).thenReturn(List.of());

        UserException ex = assertThrows(UserException.class, () -> useCase.execute(null));
        assertTrue(ex.getMessage().contains("Usuário nao encontrado"));

        verify(userRepository).findAll();
        verify(userRepository, never()).findByNameContainingIgnoreCase(anyString());
        verifyNoInteractions(userMapper);
        verifyNoInteractions(userMapper);
    }

    @Test
    void execute_ShouldThrowUserException_WhenNoUsersFound_WithName() {
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(List.of());

        UserException ex = assertThrows(UserException.class, () -> useCase.execute("John"));
        assertTrue(ex.getMessage().contains("Usuário nao encontrado"));
        assertTrue(ex.getMessage().contains("John"));

        verify(userRepository, never()).findAll();
        verify(userRepository).findByNameContainingIgnoreCase("John");
        verifyNoInteractions(userMapper);
        verifyNoInteractions(userMapper);
    }
}
