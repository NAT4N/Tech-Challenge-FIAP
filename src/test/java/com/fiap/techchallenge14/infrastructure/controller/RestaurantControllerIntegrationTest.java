package com.fiap.techchallenge14.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long restaurantId;

    @BeforeAll
    void setUp() throws Exception {
        UserCreateRequestDTO request = new UserCreateRequestDTO(
                "João da Silva",
                "joao@email.com",
                "123456",
                "Rua das Flores, 123",
                "joao.silva",
                1L
        );

        String responseUser = mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(responseUser).get("id").asLong();

        RestaurantCreateRequestDTO requestRetaurante = new RestaurantCreateRequestDTO(
                "Restaurante Teste",
                "Rua das Flores, 123",
                "Italiana",
                "Seg a Sex das 11:00 às 22:00",
                userId
        );

        String responseRestaurante = mockMvc.perform(post("/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRetaurante)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        restaurantId = objectMapper.readTree(responseRestaurante).get("id").asLong();

    }

    @Test
    void shouldCreateAndFindRestaurant() throws Exception {

        RestaurantCreateRequestDTO request = new RestaurantCreateRequestDTO(
                "Restaurante Bom",
                "Avenida Paulista, 1000",
                "Cozinha Brasileira",
                "08:00 às 22:00",
                1L
        );

        String response = mockMvc.perform(post("/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Restaurante Bom"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        RestaurantResponseDTO dto =
                objectMapper.readValue(response, RestaurantResponseDTO.class);

        mockMvc.perform(get("/v1/restaurants/" + dto.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurante Bom"));
    }

    @Test
    void shouldUpdateRestaurant() throws Exception {

        RestaurantUpdateRequestDTO update = new RestaurantUpdateRequestDTO(
                "Restaurante Atualizado",
                "Rua Nova, 456",
                "Japonesa",
                "11:00 às 23:00",
                1L
        );

        mockMvc.perform(patch("/v1/restaurants/" + restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurante Atualizado"));
    }


    @Test
    void shouldDeleteRestaurant() throws Exception {

        RestaurantCreateRequestDTO create = new RestaurantCreateRequestDTO(
                "Restaurante Delete",
                "Rua Delete",
                "Cozinha X",
                "09:00 às 18:00",
                1L
        );

        String response = mockMvc.perform(post("/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RestaurantResponseDTO dto =
                objectMapper.readValue(response, RestaurantResponseDTO.class);

        mockMvc.perform(delete("/v1/restaurants/" + dto.id()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/restaurants/" + dto.id()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindAllRestaurants() throws Exception {
        mockMvc.perform(get("/v1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
