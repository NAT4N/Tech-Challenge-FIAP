package com.fiap.techchallenge14.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.application.usecase.role.*;
import com.fiap.techchallenge14.infrastructure.dto.RoleRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RoleResponseDTO;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
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

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
class RoleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateRoleUseCase createRoleUseCase;

    @MockitoBean
    private UpdateRoleUseCase updateRoleUseCase;

    @MockitoBean
    private DeleteRoleUseCase deleteRoleUseCase;

    @MockitoBean
    private FindAllRolesUseCase findAllRolesUseCase;

    @MockitoBean
    private FindRoleByIdUseCase findRoleByIdUseCase;

    @MockitoBean
    private InMemoryToken inMemoryToken;

    @MockitoBean
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private RoleResponseDTO roleResponse;

    @BeforeEach
    void setUp() {
        roleResponse = new RoleResponseDTO(1L, "CLIENT");
    }

    @Test
    void createRole_ShouldReturn201() throws Exception {
        RoleRequestDTO request = new RoleRequestDTO("CLIENT");

        when(createRoleUseCase.execute(any(RoleRequestDTO.class))).thenReturn(roleResponse);

        mockMvc.perform(post("/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("CLIENT"));
    }

    @Test
    void updateRole_ShouldReturn200() throws Exception {
        RoleRequestDTO request = new RoleRequestDTO("ADMIN");
        RoleResponseDTO updated = new RoleResponseDTO(1L, "ADMIN");

        when(updateRoleUseCase.execute(eq(1L), any(RoleRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(patch("/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    void deleteRole_ShouldReturn204() throws Exception {
        doNothing().when(deleteRoleUseCase).execute(1L);

        mockMvc.perform(delete("/v1/roles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllRoles_ShouldReturnList() throws Exception {
        when(findAllRolesUseCase.execute()).thenReturn(List.of(roleResponse));

        mockMvc.perform(get("/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("CLIENT"));
    }

    @Test
    void getRoleById_ShouldReturn200() throws Exception {
        when(findRoleByIdUseCase.execute(1L)).thenReturn(roleResponse);

        mockMvc.perform(get("/v1/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("CLIENT"));
    }
}
