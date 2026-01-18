package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantMapper;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRestaurantUseCase {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantOwnerValidator restaurantOwnerValidator;

    @Transactional
    public RestaurantResponseDTO execute(RestaurantCreateRequestDTO dto) {

        UserEntity owner = restaurantOwnerValidator.loadRestaurantOwnerOrThrow(dto.ownerId());

        RestaurantEntity restaurantEntity = restaurantMapper.toDomain(dto);
        restaurantEntity.setOwner(owner);

        RestaurantEntity saved = restaurantRepository.save(restaurantEntity);
        log.info("Restaurante criado com o ID: {}", saved.getId());

        return restaurantMapper.toResponseDTO(saved);
    }
}
