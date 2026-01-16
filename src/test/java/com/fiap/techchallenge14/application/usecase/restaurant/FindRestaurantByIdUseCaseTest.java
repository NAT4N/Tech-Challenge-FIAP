package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantMapper;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindRestaurantByIdUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private FindRestaurantByIdUseCase useCase;

    private RestaurantEntity restaurantEntity;
    private RestaurantResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("My Restaurant");

        responseDTO = new RestaurantResponseDTO(
                1L,
                "My Restaurant",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                10L,
                "Owner"
        );
    }

    @Test
    void execute_ShouldReturnRestaurant_WhenFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(responseDTO);

        RestaurantResponseDTO result = useCase.execute(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("My Restaurant", result.name());

        verify(restaurantRepository).findById(1L);
        verify(restaurantMapper).toResponseDTO(restaurantEntity);
    }

    @Test
    void execute_ShouldThrowRestaurantException_WhenNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        RestaurantException ex =
                assertThrows(RestaurantException.class, () -> useCase.execute(99L));

        assertTrue(ex.getMessage().contains("Restaurante não encontrado com o ID: 99"));

        verify(restaurantRepository).findById(99L);
        verifyNoInteractions(restaurantMapper);
    }
}
