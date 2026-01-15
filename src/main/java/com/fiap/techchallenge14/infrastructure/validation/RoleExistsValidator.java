package com.fiap.techchallenge14.infrastructure.validation;

import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleExistsValidator implements ConstraintValidator<RoleExists, Long> {

    private final RoleRepository roleRepository;

    @Override
    public boolean isValid(Long roleId, ConstraintValidatorContext context) {
        if (roleId == null) {
            return true;
        }
        return roleRepository.findById(roleId).isPresent();
    }
}
