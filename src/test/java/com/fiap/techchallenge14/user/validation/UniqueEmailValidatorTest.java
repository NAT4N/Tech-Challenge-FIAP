package com.fiap.techchallenge14.user.validation;
/*
import com.fiap.techchallenge14.infrastructure.validation.UniqueEmailValidator;
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
class UniqueEmailValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConstraintValidatorContext context;

    private UniqueEmailValidator validator;

    @BeforeEach
    void setup() {
        validator = new UniqueEmailValidator(userRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenEmailIsUnique() {
        // Arrange
        String email = "unique@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        boolean result = validator.isValid(email, context);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenEmailAlreadyExists() {
        // Arrange
        String email = "existing@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(com.fiap.techchallenge14.user.model.User.class)));

        // Act
        boolean result = validator.isValid(email, context);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenEmailIsNull() {
        // Act
        boolean result = validator.isValid(null, context);

        // Assert
        assertTrue(result);
        verifyNoInteractions(userRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenEmailIsBlank() {
        // Act
        boolean result = validator.isValid("   ", context);

        // Assert
        assertTrue(result);
        verifyNoInteractions(userRepository);
    }
}*/
