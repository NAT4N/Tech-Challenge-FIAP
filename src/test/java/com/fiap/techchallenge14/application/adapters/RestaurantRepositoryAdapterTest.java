package com.fiap.techchallenge14.application.adapters;

import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantEntityMapper;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryAdapterTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantEntityMapper mapper;

    @InjectMocks
    private RestaurantRepositoryAdapter adapter;

    private RestaurantEntity entity;

    @BeforeEach
    void setUp() {
        entity = new RestaurantEntity();
        entity.setId(1L);
        entity.setName("Restaurante");
    }

    @Test
    void save_ShouldReturnSavedRestaurant() {
        when(mapper.toEntity(entity)).thenReturn(entity);
        when(restaurantRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(entity);

        RestaurantEntity result = adapter.save(entity);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void findById_ShouldReturnRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(entity);

        Optional<RestaurantEntity> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void findAll_ShouldReturnList() {
        when(restaurantRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(entity);

        List<RestaurantEntity> result = adapter.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void deleteById_ShouldCallRepository() {
        adapter.deleteById(1L);
        verify(restaurantRepository).deleteById(1L);
    }
}
