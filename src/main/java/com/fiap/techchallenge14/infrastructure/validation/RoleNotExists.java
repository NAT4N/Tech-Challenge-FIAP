package com.fiap.techchallenge14.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleNotExistsByNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleNotExists {

    String message() default "Já existe um tipo de usuário com esse nome";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

