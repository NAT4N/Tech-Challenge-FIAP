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
class UniqueEmailValidatorTest {

    @Mock
    private UserRepository userRepository;

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
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(new UserEntity()));
        assertFalse(validator.isValid("test@test.com", context));
    }
}
