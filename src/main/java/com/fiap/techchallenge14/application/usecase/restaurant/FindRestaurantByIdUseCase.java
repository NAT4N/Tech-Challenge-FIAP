package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.domain.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantMapper;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindRestaurantByIdUseCase {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional(readOnly = true)
    public RestaurantResponseDTO execute(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantMapper::toResponseDTO)
                .orElseThrow(() -> new RestaurantException("Restaurante não encontrado com o ID: " + id));
    }
}
