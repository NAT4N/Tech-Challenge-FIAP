package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
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
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private RestaurantOwnerValidator restaurantOwnerValidator;

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
    void execute_ShouldUpdateRestaurant_WhenOwnerNotChanged() {
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

        verify(restaurantRepository).findById(1L);
        verify(restaurantMapper).updateDomainFromDto(dto, restaurantEntity);
        verifyNoInteractions(restaurantOwnerValidator);
        verify(restaurantRepository).save(restaurantEntity);
    }

    @Test
    void execute_ShouldUpdateRestaurant_AndChangeOwner_WhenOwnerIsValid() {
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
        when(restaurantOwnerValidator.loadRestaurantOwnerOrThrow(20L)).thenReturn(newOwner);
        when(restaurantRepository.save(restaurantEntity)).thenReturn(restaurantEntity);
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(responseDTO);

        useCase.execute(1L, dto);

        assertEquals(20L, restaurantEntity.getOwner().getId());

        verify(restaurantOwnerValidator).loadRestaurantOwnerOrThrow(20L);
        verify(restaurantRepository).save(restaurantEntity);
    }

    @Test
    void execute_ShouldThrowException_WhenRestaurantNotFound() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO(
                "New Name",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                null
        );

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantException.class, () -> useCase.execute(1L, dto));

        verify(restaurantRepository).findById(1L);
        verifyNoInteractions(restaurantMapper);
        verifyNoInteractions(restaurantOwnerValidator);
    }

    @Test
    void execute_ShouldThrowException_WhenOwnerIsInvalid() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO(
                "New Name",
                "Rua A, 123",
                "Italiana",
                "10:00-22:00",
                99L
        );

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantOwnerValidator.loadRestaurantOwnerOrThrow(99L))
                .thenThrow(new RestaurantException("Usuário 99 não possui a role RESTAURANT_OWNER."));

        RestaurantException ex =
                assertThrows(RestaurantException.class, () -> useCase.execute(1L, dto));

        assertTrue(ex.getMessage().contains("RESTAURANT_OWNER"));

        verify(restaurantOwnerValidator).loadRestaurantOwnerOrThrow(99L);
        verify(restaurantRepository, never()).save(any());
    }
}
