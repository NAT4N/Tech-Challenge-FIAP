package com.fiap.techchallenge14.infrastructure.validation;

import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleNotExistsByNameValidator
        implements ConstraintValidator<RoleNotExists, String> {

    private final RoleRepository roleRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) {
            return true;
        }

        return !roleRepository.existsByName(name);
    }
}
