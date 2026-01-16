package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantUpdateRequestDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private UpdateRestaurantUseCase useCase;

    private RestaurantEntity restaurantEntity;
    private RestaurantResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        UserEntity owner = new UserEntity();
        owner.setId(10L);
        owner.setName("Owner");

        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Old Name");
        restaurantEntity.setOwner(owner);

        responseDTO = new RestaurantResponseDTO(
                1L,
                "New Name",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                10L,
                "Owner"
        );
    }

    @Test
    void execute_ShouldUpdateRestaurant_WhenOwnerIsNotChanged() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO(
                "New Name",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                null
        );

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(restaurantEntity)).thenReturn(restaurantEntity);
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(responseDTO);

        RestaurantResponseDTO result = useCase.execute(1L, dto);

        assertNotNull(result);
        assertEquals(1L, result.id());

        verify(restaurantRepository).findById(1L);
        verify(restaurantMapper).updateDomainFromDto(dto, restaurantEntity);
        verify(userRepository, never()).findById(any());
        verify(restaurantRepository).save(restaurantEntity);
        verify(restaurantMapper).toResponseDTO(restaurantEntity);
    }

    @Test
    void execute_ShouldUpdateRestaurant_AndChangeOwner_WhenOwnerIdProvided() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO(
                "New Name",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                20L
        );

        UserEntity newOwner = new UserEntity();
        newOwner.setId(20L);
        newOwner.setName("New Owner");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(userRepository.findById(20L)).thenReturn(Optional.of(newOwner));
        when(restaurantRepository.save(restaurantEntity)).thenReturn(restaurantEntity);
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(responseDTO);

        RestaurantResponseDTO result = useCase.execute(1L, dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(20L, restaurantEntity.getOwner().getId());

        verify(restaurantRepository).findById(1L);
        verify(restaurantMapper).updateDomainFromDto(dto, restaurantEntity);
        verify(userRepository).findById(20L);
        verify(restaurantRepository).save(restaurantEntity);
        verify(restaurantMapper).toResponseDTO(restaurantEntity);
    }

    @Test
    void execute_ShouldThrowRestaurantException_WhenRestaurantNotFound() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO(
                "New Name",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                null
        );

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        RestaurantException ex =
                assertThrows(RestaurantException.class, () -> useCase.execute(1L, dto));

        assertTrue(ex.getMessage().contains("Restaurante não encontrado com o ID: 1"));

        verify(restaurantRepository).findById(1L);
        verifyNoInteractions(restaurantMapper);
        verifyNoInteractions(userRepository);
    }

    @Test
    void execute_ShouldThrowRestaurantException_WhenOwnerNotFound() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO(
                "New Name",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                99L
        );

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RestaurantException ex =
                assertThrows(RestaurantException.class, () -> useCase.execute(1L, dto));

        assertTrue(ex.getMessage().contains("Proprietário não encontrado com o ID: 99"));

        verify(restaurantRepository).findById(1L);
        verify(restaurantMapper).updateDomainFromDto(dto, restaurantEntity);
        verify(userRepository).findById(99L);
        verify(restaurantRepository, never()).save(any());
        verify(restaurantMapper, never()).toResponseDTO(any());
    }
}
