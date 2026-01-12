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
class UniqueLoginValidatorTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private ConstraintValidatorContext context;

    private UniqueLoginValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueLoginValidator(userRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenLoginIsNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenLoginIsBlank() {
        assertTrue(validator.isValid("  ", context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenLoginIsUnique() {
        when(userRepository.findByLogin("johndoe")).thenReturn(Optional.empty());
        assertTrue(validator.isValid("johndoe", context));
    }

    @Test
    void isValid_ShouldReturnFalse_WhenLoginExists() {
        when(userRepository.findByLogin("johndoe")).thenReturn(Optional.of(new User()));
        assertFalse(validator.isValid("johndoe", context));
    }
}
