package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private DeleteRestaurantUseCase useCase;

    @Test
    void execute_ShouldDeleteRestaurant_WhenExists() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> useCase.execute(1L));

        verify(restaurantRepository).existsById(1L);
        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void execute_ShouldThrowRestaurantException_WhenRestaurantDoesNotExist() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);

        RestaurantException ex =
                assertThrows(RestaurantException.class, () -> useCase.execute(1L));

        assertTrue(ex.getMessage().contains("Restaurante não encontrado com o ID: 1"));

        verify(restaurantRepository).existsById(1L);
        verify(restaurantRepository, never()).deleteById(anyLong());
    }
}
