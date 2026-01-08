package com.fiap.techchallenge14.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleExists {

    String message() default "Tipo de usuário não encontrada para o ID informado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
