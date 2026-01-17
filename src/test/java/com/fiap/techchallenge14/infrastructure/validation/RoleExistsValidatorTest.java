package com.fiap.techchallenge14.infrastructure.validation;

import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
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
class RoleExistsValidatorTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ConstraintValidatorContext context;

    private RoleExistsValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RoleExistsValidator(roleRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenRoleIsNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenRoleExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(new RoleEntity(1L, "CLIENT")));
        assertTrue(validator.isValid(1L, context));
    }

    @Test
    void isValid_ShouldReturnFalse_WhenRoleNotExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(validator.isValid(1L, context));
    }
}
