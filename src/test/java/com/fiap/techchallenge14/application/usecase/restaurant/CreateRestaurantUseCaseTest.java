package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantMapper;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
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
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private CreateRestaurantUseCase useCase;

    private UserEntity owner;
    private RestaurantEntity restaurantEntity;
    private RestaurantEntity savedEntity;
    private RestaurantResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        owner = new UserEntity();
        owner.setId(10L);
        owner.setName("Owner");

        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setName("My Restaurant");
        restaurantEntity.setAddress("Rua A, 123");
        restaurantEntity.setCuisineType("Italiana");
        restaurantEntity.setOpeningHours("10:00-22:00");

        savedEntity = new RestaurantEntity();
        savedEntity.setId(1L);
        savedEntity.setName("My Restaurant");
        savedEntity.setAddress("Rua A, 123");
        savedEntity.setCuisineType("Italiana");
        savedEntity.setOpeningHours("10:00-22:00");
        savedEntity.setOwner(owner);

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
    void execute_ShouldCreateRestaurant_WhenOwnerExists() {
        RestaurantCreateRequestDTO dto = new RestaurantCreateRequestDTO(
                "My Restaurant",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                10L
        );

        when(userRepository.findById(10L)).thenReturn(Optional.of(owner));
        when(restaurantMapper.toDomain(dto)).thenReturn(restaurantEntity);
        when(restaurantRepository.save(restaurantEntity)).thenReturn(savedEntity);
        when(restaurantMapper.toResponseDTO(savedEntity)).thenReturn(responseDTO);

        RestaurantResponseDTO result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("My Restaurant", result.name());
        assertEquals(10L, result.ownerId());
        assertEquals("Owner", result.ownerName());

        assertNotNull(restaurantEntity.getOwner());
        assertEquals(10L, restaurantEntity.getOwner().getId());

        verify(userRepository).findById(10L);
        verify(restaurantMapper).toDomain(dto);
        verify(restaurantRepository).save(restaurantEntity);
        verify(restaurantMapper).toResponseDTO(savedEntity);
    }

    @Test
    void execute_ShouldThrowRestaurantException_WhenOwnerNotFound() {
        RestaurantCreateRequestDTO dto = new RestaurantCreateRequestDTO(
                "My Restaurant",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                10L
        );

        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        RestaurantException ex = assertThrows(RestaurantException.class, () -> useCase.execute(dto));
        assertTrue(ex.getMessage().contains("Proprietário não encontrado com o ID: 10"));

        verify(userRepository).findById(10L);
        verifyNoInteractions(restaurantMapper);
        verifyNoInteractions(restaurantRepository);
    }
}
