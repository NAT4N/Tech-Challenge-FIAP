package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantMapper;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllRestaurantsUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private FindAllRestaurantsUseCase useCase;

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
    void execute_ShouldReturnAllRestaurants_WhenExist() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurantEntity));
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(responseDTO);

        List<RestaurantResponseDTO> result = useCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals("My Restaurant", result.getFirst().name());

        verify(restaurantRepository).findAll();
        verify(restaurantMapper).toResponseDTO(restaurantEntity);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenNoRestaurantsExist() {
        when(restaurantRepository.findAll()).thenReturn(List.of());

        List<RestaurantResponseDTO> result = useCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restaurantRepository).findAll();
        verifyNoInteractions(restaurantMapper);
    }
}
