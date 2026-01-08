package com.fiap.techchallenge14.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantCreateRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String name,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(min = 5, max = 255, message = "O endereço deve ter entre 5 e 255 caracteres")
        String address,

        @NotBlank(message = "O tipo de cozinha é obrigatório")
        @Size(min = 3, max = 50, message = "O tipo de cozinha deve ter entre 3 e 50 caracteres")
        String cuisineType,

        @NotBlank(message = "O horário de funcionamento é obrigatório")
        String openingHours,

        @NotNull(message = "O proprietário é obrigatório")
        Long ownerId
) {
}
