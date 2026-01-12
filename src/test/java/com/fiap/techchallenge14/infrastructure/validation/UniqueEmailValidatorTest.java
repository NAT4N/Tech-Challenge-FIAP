package com.fiap.techchallenge14.infrastructure.validation;

import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.domain.model.User;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniqueEmailValidatorTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private ConstraintValidatorContext context;

    private UniqueEmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueEmailValidator(userRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenEmailIsNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenEmailIsBlank() {
        assertTrue(validator.isValid("  ", context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenEmailIsUnique() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        assertTrue(validator.isValid("test@test.com", context));
    }

    @Test
    void isValid_ShouldReturnFalse_WhenEmailExists() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(new User()));
        assertFalse(validator.isValid("test@test.com", context));
    }
}
