package com.fiap.techchallenge14.domain.dto;

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
