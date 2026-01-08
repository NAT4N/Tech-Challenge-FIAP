package com.fiap.techchallenge14.application.port.out;

import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepositoryPort {
    RestaurantEntity save(RestaurantEntity restaurantEntity);
    Optional<RestaurantEntity> findById(Long id);
    List<RestaurantEntity> findAll();
    void deleteById(Long id);
}
