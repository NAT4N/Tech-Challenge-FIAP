package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toResponseDTO_ShouldMapCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setRole(RoleType.CLIENT);

        UserResponseDTO response = mapper.toResponseDTO(user);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals("CLIENT", response.roleName());
    }

    @Test
    void toDomain_ShouldMapCorrectly() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO(
                "John Doe", "john@example.com", "password", "Address 1", "johndoe", 1L
        );

        User user = mapper.toDomain(dto);

        assertNotNull(user);
        assertEquals(dto.name(), user.getName());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.password(), user.getPassword());
        assertEquals(dto.address(), user.getAddress());
        assertEquals(dto.login(), user.getLogin());
        assertNull(user.getRole());
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setActive(true);

        UserEntity entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getName(), entity.getName());
        assertEquals(user.getEmail(), entity.getEmail());
        assertTrue(entity.getActive());
    }

    @Test
    void updateDomainFromDto_ShouldUpdateCorrectly() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old@example.com");

        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "New Name", "new@example.com", "New Address", "newlogin", 1L
        );

        mapper.updateDomainFromDto(dto, user);

        assertEquals(dto.name(), user.getName());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.address(), user.getAddress());
        assertEquals(dto.login(), user.getLogin());
    }

    @Test
    void toResponseDTO_WithNull_ShouldReturnNull() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    void toDomain_WithNull_ShouldReturnNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_WithNull_ShouldReturnNull() {
        assertNull(mapper.toEntity(null));
    }
}
