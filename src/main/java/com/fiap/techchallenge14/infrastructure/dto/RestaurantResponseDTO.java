package com.fiap.techchallenge14.infrastructure.dto;

public record RestaurantResponseDTO(
        Long id,
        String name,
        String address,
        String cuisineType,
        String openingHours,
        Long ownerId,
        String ownerName
) {
}
