package com.fiap.techchallenge14.restaurant.controller;

import com.fiap.techchallenge14.application.port.in.RestaurantUsecase;
import com.fiap.techchallenge14.domain.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.controller.RestaurantController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RestaurantUsecase restaurantUsecase;

    @InjectMocks
    private RestaurantController restaurantController;

    private RestaurantResponseDTO restaurantResponse;

    @BeforeEach
    void setup() {
        restaurantResponse = new RestaurantResponseDTO(
                1L, "Restaurant Name", "Restaurant Address", "Italian", "08:00 - 22:00", 1L, "Owner Name");
    }

    @Test
    void createRestaurant_ShouldReturnCreatedRestaurant() {
        RestaurantCreateRequestDTO request = new RestaurantCreateRequestDTO(
                "Restaurant Name", "Restaurant Address", "Italian", "08:00 - 22:00", 1L);

        when(restaurantUsecase.save(request)).thenReturn(restaurantResponse);

        ResponseEntity<RestaurantResponseDTO> response = restaurantController.createRestaurant(request);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Restaurant Name", response.getBody().name());
        verify(restaurantUsecase).save(request);
    }

    @Test
    void updateRestaurant_ShouldReturnUpdatedRestaurant() {
        RestaurantUpdateRequestDTO request = new RestaurantUpdateRequestDTO(
                "New Name", null, null, null, null);
        RestaurantResponseDTO updatedResponse = new RestaurantResponseDTO(
                1L, "New Name", "Restaurant Address", "Italian", "08:00 - 22:00", 1L, "Owner Name");

        when(restaurantUsecase.update(1L, request)).thenReturn(updatedResponse);

        ResponseEntity<RestaurantResponseDTO> response = restaurantController.updateRestaurant(1L, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("New Name", response.getBody().name());
        verify(restaurantUsecase).update(1L, request);
    }

    @Test
    void deleteRestaurant_ShouldReturnNoContent() {
        doNothing().when(restaurantUsecase).delete(1L);

        ResponseEntity<Void> response = restaurantController.deleteRestaurant(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(restaurantUsecase).delete(1L);
    }

    @Test
    void getAllRestaurants_ShouldReturnList() {
        when(restaurantUsecase.findAll()).thenReturn(List.of(restaurantResponse));

        ResponseEntity<List<RestaurantResponseDTO>> response = restaurantController.getAllRestaurants();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(restaurantUsecase).findAll();
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurant() {
        when(restaurantUsecase.findById(1L)).thenReturn(restaurantResponse);

        ResponseEntity<RestaurantResponseDTO> response = restaurantController.getRestaurantById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().id());
        verify(restaurantUsecase).findById(1L);
    }
}
