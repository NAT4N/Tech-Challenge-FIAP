package com.fiap.techchallenge14.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.application.port.in.RestaurantUsecase;
import com.fiap.techchallenge14.application.port.out.TokenMemoryPort;
import com.fiap.techchallenge14.domain.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantUpdateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantUsecase restaurantUsecase;

    @MockitoBean
    private TokenMemoryPort tokenMemoryPort;

    @Autowired
    private ObjectMapper objectMapper;

    private RestaurantResponseDTO restaurantResponse;

    @BeforeEach
    void setUp() {
        restaurantResponse = new RestaurantResponseDTO(
                1L, "Rest", "Addr", "Cuis", "Hours", 1L, "Owner"
        );
    }

    @Test
    void createRestaurant_ShouldReturn201() throws Exception {
        RestaurantCreateRequestDTO request =
                new RestaurantCreateRequestDTO(
                        "Restaurante Bom",
                        "Avenida Paulista, 1000",
                        "Cozinha Brasileira",
                        "08:00 às 22:00",
                        1L
                );

        when(restaurantUsecase.save(any())).thenReturn(restaurantResponse);

        mockMvc.perform(post("/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateRestaurant_ShouldReturn200() throws Exception {
        RestaurantUpdateRequestDTO request =
                new RestaurantUpdateRequestDTO(
                        "Novo Restaurante",
                        "Rua das Palmeiras, 456",
                        "Cozinha Japonesa",
                        "11:00 às 23:00",
                        1L
                );

        when(restaurantUsecase.update(eq(1L), any())).thenReturn(restaurantResponse);

        mockMvc.perform(patch("/v1/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void deleteRestaurant_ShouldReturn204() throws Exception {
        doNothing().when(restaurantUsecase).delete(1L);

        mockMvc.perform(delete("/v1/restaurants/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllRestaurants_ShouldReturnList() throws Exception {
        when(restaurantUsecase.findAll()).thenReturn(List.of(restaurantResponse));

        mockMvc.perform(get("/v1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurant() throws Exception {
        when(restaurantUsecase.findById(1L)).thenReturn(restaurantResponse);

        mockMvc.perform(get("/v1/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
