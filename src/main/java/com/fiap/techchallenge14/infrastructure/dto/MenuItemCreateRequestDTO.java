package com.fiap.techchallenge14.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MenuItemCreateRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "A disponibilidade para consumo local é obrigatória")
        Boolean availableOnlyInRestaurant,

        String photoPath,

        @NotNull(message = "O ID do restaurante é obrigatório")
        Long restaurantId
) {
}
