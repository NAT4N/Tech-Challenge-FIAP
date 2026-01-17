package com.fiap.techchallenge14.infrastructure.dto;

import java.math.BigDecimal;

public record MenuItemResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean availableOnlyInRestaurant,
        String photoPath,
        Long restaurantId
) {
}
