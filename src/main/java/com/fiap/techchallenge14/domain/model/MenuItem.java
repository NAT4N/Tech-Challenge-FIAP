package com.fiap.techchallenge14.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItem {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean availableOnlyInRestaurant;
    private String photoPath;
    private Long restaurantId;
}
