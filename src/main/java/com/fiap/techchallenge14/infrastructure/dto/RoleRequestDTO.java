package com.fiap.techchallenge14.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDTO(
        @NotBlank(message = "O nome do tipo é obrigatório")
        String name
) {
}