package com.fiap.techchallenge14.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueLoginValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLogin {
    String message() default "Login já está em uso";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
