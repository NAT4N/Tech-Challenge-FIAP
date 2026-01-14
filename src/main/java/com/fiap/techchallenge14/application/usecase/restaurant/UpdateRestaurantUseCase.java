package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.domain.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantUpdateRequestDTO;
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
public class UpdateRestaurantUseCase {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public RestaurantResponseDTO execute(Long id, RestaurantUpdateRequestDTO dto) {

        RestaurantEntity restaurantEntity = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantException("Restaurante não encontrado com o ID: " + id));

        restaurantMapper.updateDomainFromDto(dto, restaurantEntity);

        if (dto.ownerId() != null) {
            UserEntity owner = userRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new RestaurantException("Proprietário não encontrado com o ID: " + dto.ownerId()));

            restaurantEntity.setOwner(owner);
        }

        RestaurantEntity updated = restaurantRepository.save(restaurantEntity);
        log.info("Restaurante atualizado com o ID: {}", updated.getId());

        return restaurantMapper.toResponseDTO(updated);
    }
}
