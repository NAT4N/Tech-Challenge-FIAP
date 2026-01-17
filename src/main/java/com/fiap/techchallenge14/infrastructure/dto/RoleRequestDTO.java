package com.fiap.techchallenge14.infrastructure.dto;

import com.fiap.techchallenge14.infrastructure.validation.RoleNotExists;
import jakarta.validation.constraints.NotBlank;

public record RoleRequestDTO(
        @NotBlank(message = "O nome do tipo de usuário é obrigatório")
        @RoleNotExists
        String name
) {
}