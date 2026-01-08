package com.fiap.techchallenge14.application.adapters;

import com.fiap.techchallenge14.application.port.out.RestaurantRepositoryPort;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantEntityMapper;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper mapper;

    @Override
    public RestaurantEntity save(RestaurantEntity restaurantEntity) {
        var entity = mapper.toEntity(restaurantEntity);
        var saved = restaurantRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RestaurantEntity> findById(Long id) {
        return restaurantRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<RestaurantEntity> findAll() {
        return restaurantRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        restaurantRepository.deleteById(id);
    }
}
