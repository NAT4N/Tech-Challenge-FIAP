package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteRestaurantUseCase {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void execute(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new RestaurantException("Restaurante não encontrado com o ID: " + id);
        }
        restaurantRepository.deleteById(id);
        log.info("Restaurante deletado com o ID: {}", id);
    }
}
