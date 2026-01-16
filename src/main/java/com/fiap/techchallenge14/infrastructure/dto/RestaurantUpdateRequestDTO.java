package com.fiap.techchallenge14.infrastructure.dto;

import jakarta.validation.constraints.Size;

public record RestaurantUpdateRequestDTO(
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String name,

        @Size(min = 5, max = 255, message = "O endereço deve ter entre 5 e 255 caracteres")
        String address,

        @Size(min = 3, max = 50, message = "O tipo de cozinha deve ter entre 3 e 50 caracteres")
        String cuisineType,

        String openingHours,

        Long ownerId
) {
}
