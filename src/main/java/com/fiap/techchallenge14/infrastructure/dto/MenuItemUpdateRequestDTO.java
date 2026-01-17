package com.fiap.techchallenge14.infrastructure.dto;

import java.math.BigDecimal;

public record MenuItemUpdateRequestDTO(
        String name,
        String description,
        BigDecimal price,
        Boolean availableOnlyInRestaurant,
        String photoPath
) {
}
