package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private final UserEntityMapper mapper =
            Mappers.getMapper(UserEntityMapper.class);

    @Test
    void toDomain_ShouldMapCorrectly_WhenRoleExists() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setName("John Doe");

        RoleEntity role = new RoleEntity();
        role.setId(2L);
        role.setName("CLIENT");

        entity.setRole(role);

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals(2L, user.getRoleId());
    }

    @Test
    void toDomain_ShouldSetNullRoleId_WhenRoleIsNull() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setRole(null);

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertNull(user.getRoleId());
    }

    @Test
    void toDomain_ShouldSetNullRoleId_WhenRoleIdIsNull() {
        UserEntity entity = new UserEntity();

        RoleEntity role = new RoleEntity();
        role.setId(null);
        entity.setRole(role);

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertNull(user.getRoleId());
    }

    @Test
    void toEntity_ShouldMapCorrectly_WhenRoleIdExists() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setRoleId(3L);

        UserEntity entity = mapper.toEntity(user);
        entity.setRole(new RoleEntity(3L, "CLIENT"));

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("John Doe", entity.getName());

        assertNotNull(entity.getRole());
        assertEquals(3L, entity.getRole().getId());
    }

    @Test
    void toEntity_ShouldSetNullRole_WhenRoleIdIsNull() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setRoleId(null);

        UserEntity entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertNull(entity.getRole());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateBasicFields_AndRoleId() {
        UserEntity entity = new UserEntity();
        entity.setName("Old Name");

        User user = new User();
        user.setName("New Name");
        user.setRoleId(5L);

        mapper.updateEntityFromDomain(user, entity);

        entity.setRole(new RoleEntity(5L, "CLIENT"));

        assertEquals("New Name", entity.getName());
        assertNotNull(entity.getRole());
        assertEquals(5L, entity.getRole().getId());
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
