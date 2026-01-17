package com.fiap.techchallenge14.infrastructure.validation;

import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleNotExistsByNameValidatorTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ConstraintValidatorContext context;

    private RoleNotExistsByNameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RoleNotExistsByNameValidator(roleRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenNameIsNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenNameIsBlank() {
        assertTrue(validator.isValid("   ", context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenRoleDoesNotExist() {
        when(roleRepository.existsByName("ADMIN")).thenReturn(false);

        assertTrue(validator.isValid("ADMIN", context));
    }

    @Test
    void isValid_ShouldReturnFalse_WhenRoleAlreadyExists() {
        when(roleRepository.existsByName("CLIENT")).thenReturn(true);

        assertFalse(validator.isValid("CLIENT", context));
    }
}
