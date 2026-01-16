package com.fiap.techchallenge14.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.application.usecase.restaurant.*;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.security.InMemoryToken;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateRestaurantUseCase createRestaurantUseCase;

    @MockitoBean
    private DeleteRestaurantUseCase deleteRestaurantUseCase;

    @MockitoBean
    private FindAllRestaurantsUseCase findAllRestaurantsUseCase;

    @MockitoBean
    private FindRestaurantByIdUseCase findRestaurantByIdUseCase;

    @MockitoBean
    private UpdateRestaurantUseCase updateRestaurantUseCase;

    @MockitoBean
    private InMemoryToken inMemoryToken;

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

        when(createRestaurantUseCase.execute(any())).thenReturn(restaurantResponse);

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

        when(updateRestaurantUseCase.execute(eq(1L), any())).thenReturn(restaurantResponse);

        mockMvc.perform(patch("/v1/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void deleteRestaurant_ShouldReturn204() throws Exception {
        doNothing().when(deleteRestaurantUseCase).execute(1L);

        mockMvc.perform(delete("/v1/restaurants/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllRestaurants_ShouldReturnList() throws Exception {
        when(findAllRestaurantsUseCase.execute()).thenReturn(List.of(restaurantResponse));

        mockMvc.perform(get("/v1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurant() throws Exception {
        when(findRestaurantByIdUseCase.execute(1L)).thenReturn(restaurantResponse);

        mockMvc.perform(get("/v1/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
