package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantMapper;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllRestaurantsUseCase {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> execute() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponseDTO)
                .toList();
    }
}
