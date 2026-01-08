package com.fiap.techchallenge14.role.validation;

import com.fiap.techchallenge14.application.port.out.RoleRepositoryPort;
import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.validation.RoleExistsValidator;
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
class RoleEntityExistsValidatorTest {

    @Mock
    private RoleRepositoryPort roleRepository;

    @Mock
    private ConstraintValidatorContext context;

    private RoleExistsValidator validator;

    @BeforeEach
    void setup() {
        validator = new RoleExistsValidator(roleRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenRoleExists() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(new Role()));

        // Act
        boolean result = validator.isValid(roleId, context);

        // Assert
        assertTrue(result);
        verify(roleRepository, times(1)).findById(roleId);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenRoleDoesNotExist() {
        // Arrange
        Long roleId = 2L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act
        boolean result = validator.isValid(roleId, context);

        // Assert
        assertFalse(result);
        verify(roleRepository, times(1)).findById(roleId);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenRoleIdIsNull() {
        // Act
        boolean result = validator.isValid(null, context);

        // Assert
        assertTrue(result);
        verifyNoInteractions(roleRepository);
    }
}
