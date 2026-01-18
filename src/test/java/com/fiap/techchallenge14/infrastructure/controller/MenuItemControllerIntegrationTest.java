package com.fiap.techchallenge14.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.infrastructure.dto.*;
import jakarta.transaction.Transactional;
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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
class MenuItemControllerIntegrationTest {

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
                2L
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

        MenuItemCreateRequestDTO requestItem = new MenuItemCreateRequestDTO(
                "Pizza",
                "Pizza de calabresa",
                new BigDecimal("29.90"),
                false,
                "/images/pizza.jpg",
                restaurantId
        );

        mockMvc.perform(post("/v1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pizza"))
                .andReturn()
                .getResponse()
                .getContentAsString();

    }

    @Test
    void shouldCreateAndFindMenuItem() throws Exception {

        MenuItemCreateRequestDTO request = new MenuItemCreateRequestDTO(
                "Pizza",
                "Pizza de calabresa",
                new BigDecimal("29.90"),
                false,
                "/images/pizza.jpg",
                restaurantId
        );

        String response = mockMvc.perform(post("/v1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pizza"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        MenuItemResponseDTO dto = objectMapper.readValue(response, MenuItemResponseDTO.class);

        mockMvc.perform(get("/v1/menu-items/" + dto.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void shouldFindMenuItemByRestaurant() throws Exception {
        mockMvc.perform(get("/v1/menu-items/restaurant/" + restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateMenuItem() throws Exception {

        Long id = createMenuItem();

        MenuItemUpdateRequestDTO update = new MenuItemUpdateRequestDTO(
                "Pizza Atualizada",
                null,
                null,
                null,
                null
        );

        mockMvc.perform(patch("/v1/menu-items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza Atualizada"));
    }

    @Test
    void shouldDeleteMenuItem() throws Exception {

        Long id = createMenuItem();

        mockMvc.perform(delete("/v1/menu-items/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/menu-items/" + id))
                .andExpect(status().isBadRequest());
    }

    private Long createMenuItem() throws Exception {
        MenuItemCreateRequestDTO create = new MenuItemCreateRequestDTO(
                "Pizza",
                "Pizza de calabresa",
                new BigDecimal("29.90"),
                false,
                "/images/pizza.jpg",
                restaurantId
        );

        String response = mockMvc.perform(post("/v1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MenuItemResponseDTO dto = objectMapper.readValue(response, MenuItemResponseDTO.class);
        return dto.id();
    }
}

