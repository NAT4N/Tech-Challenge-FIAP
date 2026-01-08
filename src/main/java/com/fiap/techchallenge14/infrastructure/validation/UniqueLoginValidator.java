package com.fiap.techchallenge14.infrastructure.validation;

import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

    private final UserRepositoryPort userRepository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login == null || login.isBlank()) {
            return true;
        }

        return userRepository.findByLogin(login).isEmpty();
    }
}
