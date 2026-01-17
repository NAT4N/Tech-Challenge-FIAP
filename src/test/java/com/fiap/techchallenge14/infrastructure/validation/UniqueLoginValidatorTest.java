package com.fiap.techchallenge14.infrastructure.validation;

import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniqueLoginValidatorTest {

    @Mock
    private UserRepository userRepository;

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
        when(userRepository.findByLogin("johndoe")).thenReturn(Optional.of(new UserEntity()));
        assertFalse(validator.isValid("johndoe", context));
    }
}
