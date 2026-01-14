package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.domain.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantMapper;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRestaurantUseCase {

    private final RestaurantRepository restaurantRepository; // JPA
    private final UserRepository userRepository;             // JPA
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public RestaurantResponseDTO execute(RestaurantCreateRequestDTO dto) {

        UserEntity owner = userRepository.findById(dto.ownerId())
                .orElseThrow(() -> new RestaurantException("Proprietário não encontrado com o ID: " + dto.ownerId()));

        // TODO: regra: se CLIENT => vira RESTAURANT_OWNER
//        if (owner.getRole() != null && RoleType.CLIENT.name().equals(owner.getRole().getName())) {
//            owner.getRole().setName(RoleType.RESTAURANT_OWNER.name());
//            userRepository.save(owner);
//        }

        RestaurantEntity restaurantEntity = restaurantMapper.toDomain(dto);
        restaurantEntity.setOwner(owner);

        RestaurantEntity saved = restaurantRepository.save(restaurantEntity);
        log.info("Restaurante criado com o ID: {}", saved.getId());

        return restaurantMapper.toResponseDTO(saved);
    }
}
