package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private final UserEntityMapper mapper = Mappers.getMapper(UserEntityMapper.class);

    @Test
    void toDomain_ShouldMapCorrectly() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setName("John Doe");
        RoleEntity role = new RoleEntity();
        role.setName("CLIENT");
        entity.setRole(role);

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getName(), user.getName());
        assertEquals(RoleType.CLIENT, user.getRole());
    }

    @Test
    void toDomain_WithInvalidRole_ShouldReturnNullRole() {
        UserEntity entity = new UserEntity();
        RoleEntity role = new RoleEntity();
        role.setName("INVALID_ROLE");
        entity.setRole(role);

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertNull(user.getRole());
    }

    @Test
    void toDomain_WithNullRole_ShouldReturnNullRole() {
        UserEntity entity = new UserEntity();
        entity.setRole(null);

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertNull(user.getRole());
    }

    @Test
    void toDomain_WithNullRoleName_ShouldReturnNullRole() {
        UserEntity entity = new UserEntity();
        entity.setRole(new RoleEntity());

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertNull(user.getRole());
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        UserEntity entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getName(), entity.getName());
        assertNull(entity.getRole());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateCorrectly() {
        UserEntity entity = new UserEntity();
        entity.setName("Old Name");

        User user = new User();
        user.setName("New Name");

        mapper.updateEntityFromDomain(user, entity);

        assertEquals(user.getName(), entity.getName());
        assertNull(entity.getRole());
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
