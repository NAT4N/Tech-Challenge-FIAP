package com.fiap.techchallenge14.restaurant.service;

import com.fiap.techchallenge14.application.port.out.RestaurantRepositoryPort;
import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.application.usecase.restaurant.RestaurantUsecaseImpl;
import com.fiap.techchallenge14.domain.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantUpdateRequestDTO;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantMapper;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUsecaseImplTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RestaurantUsecaseImpl restaurantUsecase;

    private RestaurantEntity restaurantEntity;
    private User owner;

    @BeforeEach
    void setup() {
        owner = new User();
        owner.setId(1L);
        owner.setName("Owner Name");

        UserEntity ownerEntity = new UserEntity();
        ownerEntity.setId(1L);
        ownerEntity.setName("Owner Name");

        restaurantEntity = RestaurantEntity.builder()
                .id(1L)
                .name("Restaurant Name")
                .address("Restaurant Address")
                .cuisineType("Italian")
                .openingHours("08:00 - 22:00")
                .owner(ownerEntity)
                .build();
    }

    @Test
    void save_ShouldCreateRestaurantAndUpgradeUserRoleSuccessfully() {
        RestaurantCreateRequestDTO dto = new RestaurantCreateRequestDTO(
                "Restaurant Name", "Restaurant Address", "Italian", "08:00 - 22:00", 1L);

        UserEntity ownerEntity = restaurantEntity.getOwner();
        owner.setRole(com.fiap.techchallenge14.domain.model.RoleType.CLIENT);

        when(restaurantMapper.toDomain(dto)).thenReturn(restaurantEntity);
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(userMapper.toEntity(owner)).thenReturn(ownerEntity);
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(new RestaurantResponseDTO(
                1L, "Restaurant Name", "Restaurant Address", "Italian", "08:00 - 22:00", 1L, "Owner Name"));

        RestaurantResponseDTO response = restaurantUsecase.save(dto);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(owner.getRole()).isEqualTo(com.fiap.techchallenge14.domain.model.RoleType.RESTAURANT_OWNER);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
        verify(userRepository).save(owner);
    }

    @Test
    void save_ShouldThrowException_WhenOwnerNotFound() {
        RestaurantCreateRequestDTO dto = new RestaurantCreateRequestDTO(
                "Restaurant Name", "Restaurant Address", "Italian", "08:00 - 22:00", 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantUsecase.save(dto))
                .isInstanceOf(RestaurantException.class)
                .hasMessageContaining("Proprietário não encontrado");
    }

    @Test
    void update_ShouldUpdateRestaurantSuccessfully() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO(
                "New Name", null, null, null, 1L);

        UserEntity ownerEntity = restaurantEntity.getOwner();

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(new RestaurantResponseDTO(
                1L, "New Name", "Restaurant Address", "Italian", "08:00 - 22:00", 1L, "Owner Name"));

        RestaurantResponseDTO response = restaurantUsecase.update(1L, dto);

        assertThat(response).isNotNull();
        verify(restaurantMapper).updateDomainFromDto(dto, restaurantEntity);
        verify(restaurantRepository).save(restaurantEntity);
    }

    @Test
    void update_ShouldThrowException_WhenRestaurantNotFound() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO(null, null, null, null, null);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantUsecase.update(1L, dto))
                .isInstanceOf(RestaurantException.class)
                .hasMessageContaining("Restaurante não encontrado");
    }

    @Test
    void delete_ShouldDeleteRestaurantSuccessfully() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));

        restaurantUsecase.delete(1L);

        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantUsecase.delete(1L))
                .isInstanceOf(RestaurantException.class)
                .hasMessageContaining("Restaurante não encontrado");
    }

    @Test
    void findAll_ShouldReturnList() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurantEntity));
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(new RestaurantResponseDTO(
                1L, "Restaurant Name", "Restaurant Address", "Italian", "08:00 - 22:00", 1L, "Owner Name"));

        List<RestaurantResponseDTO> response = restaurantUsecase.findAll();

        assertThat(response).hasSize(1);
    }

    @Test
    void findById_ShouldReturnRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(new RestaurantResponseDTO(
                1L, "Restaurant Name", "Restaurant Address", "Italian", "08:00 - 22:00", 1L, "Owner Name"));

        RestaurantResponseDTO response = restaurantUsecase.findById(1L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
    }
}
