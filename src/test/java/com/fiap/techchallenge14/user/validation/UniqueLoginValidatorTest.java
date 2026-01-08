package com.fiap.techchallenge14.user.validation;
/*
import com.fiap.techchallenge14.infrastructure.validation.UniqueLoginValidator;
import com.fiap.techchallenge14.user.model.User;
import com.fiap.techchallenge14.user.repository.UserRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniqueLoginValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConstraintValidatorContext context;

    private UniqueLoginValidator validator;

    @BeforeEach
    void setup() {
        validator = new UniqueLoginValidator(userRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenLoginIsUnique() {
        // Arrange
        String login = "uniqueLogin";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Act
        boolean result = validator.isValid(login, context);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByLogin(login);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenLoginAlreadyExists() {
        // Arrange
        String login = "existingLogin";
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(mock(User.class)));

        // Act
        boolean result = validator.isValid(login, context);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByLogin(login);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenLoginIsNull() {
        // Act
        boolean result = validator.isValid(null, context);

        // Assert
        assertTrue(result);
        verifyNoInteractions(userRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenLoginIsBlank() {
        // Act
        boolean result = validator.isValid("   ", context);

        // Assert
        assertTrue(result);
        verifyNoInteractions(userRepository);
    }
}*/
