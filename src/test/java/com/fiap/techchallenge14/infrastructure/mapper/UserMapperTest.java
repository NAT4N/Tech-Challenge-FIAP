package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserUpdateRequestDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toResponseDTO_ShouldMapCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setAddress("Address 1");
        user.setLogin("johndoe");
        user.setCreatedAt(LocalDateTime.now());
        user.setLastUpdatedAt(LocalDateTime.now());
        user.setLastLoginAt(LocalDateTime.now());
        user.setActive(true);
        user.setRoleId(10L);

        UserResponseDTO response = mapper.toResponseDTO(user);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getAddress(), response.address());
        assertEquals(user.getLogin(), response.login());
        assertEquals(user.getCreatedAt(), response.createdAt());
        assertEquals(user.getLastUpdatedAt(), response.lastUpdatedAt());
        assertEquals(user.getLastLoginAt(), response.lastLoginAt());
        assertEquals(user.getActive(), response.active());
        assertEquals(user.getRoleId(), response.roleId());
    }

    @Test
    void toDomain_ShouldMapCorrectly_FromCreateDto() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO(
                "John Doe",
                "john@example.com",
                "password",
                "Address 1",
                "johndoe",
                10L
        );

        User user = mapper.toDomain(dto);

        assertNotNull(user);
        assertEquals(dto.name(), user.getName());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.password(), user.getPassword());
        assertEquals(dto.address(), user.getAddress());
        assertEquals(dto.login(), user.getLogin());
        assertEquals(dto.roleId(), user.getRoleId());
    }

    @Test
    void updateDomainFromDto_ShouldUpdateCorrectly() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old@example.com");
        user.setAddress("Old Address");
        user.setLogin("oldlogin");
        user.setRoleId(1L);

        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "New Name",
                "new@example.com",
                "New Address",
                "newlogin",
                2L
        );

        mapper.updateDomainFromDto(dto, user);

        assertEquals(dto.name(), user.getName());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.address(), user.getAddress());
        assertEquals(dto.login(), user.getLogin());
        assertEquals(dto.roleId(), user.getRoleId());
    }

    @Test
    void toResponseDTO_WithNull_ShouldReturnNull() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    void toDomain_WithNull_ShouldReturnNull() {
        assertNull(mapper.toDomain((UserCreateRequestDTO) null));
    }
}
