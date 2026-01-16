package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.application.port.out.RestaurantRepositoryPort;
import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantUpdateRequestDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private User owner;
    private RestaurantEntity restaurantEntity;
    private RestaurantResponseDTO restaurantResponse;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setRole(RoleType.CLIENT);

        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Rest");

        restaurantResponse = new RestaurantResponseDTO(1L, "Rest", "Addr", "Cuis", "Hours", 1L, "Owner");
    }

    @Test
    void save_ShouldCreateRestaurantAndUpgradeOwner() {
        RestaurantCreateRequestDTO dto = new RestaurantCreateRequestDTO("Rest", "Addr", "Cuis", "Hours", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(restaurantMapper.toDomain(dto)).thenReturn(restaurantEntity);
        when(userMapper.toEntity(owner)).thenReturn(new UserEntity());
        when(restaurantRepository.save(any())).thenReturn(restaurantEntity);
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(restaurantResponse);

        RestaurantResponseDTO result = restaurantUsecase.save(dto);

        assertNotNull(result);
        assertEquals(RoleType.RESTAURANT_OWNER, owner.getRole());
        verify(userRepository).save(owner);
    }

    @Test
    void save_ShouldThrowException_WhenOwnerNotFound() {
        RestaurantCreateRequestDTO dto = new RestaurantCreateRequestDTO("Rest", "Addr", "Cuis", "Hours", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantException.class, () -> restaurantUsecase.save(dto));
    }

    @Test
    void update_ShouldUpdateRestaurant() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO("New", "Addr", "Cuis", "Hours", 1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(restaurantRepository.save(any())).thenReturn(restaurantEntity);
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(restaurantResponse);

        RestaurantResponseDTO result = restaurantUsecase.update(1L, dto);

        assertNotNull(result);
        verify(restaurantMapper).updateDomainFromDto(dto, restaurantEntity);
    }

    @Test
    void update_ShouldThrowException_WhenRestaurantNotFound() {
        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO("New", "Addr", "Cuis", "Hours", 1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantException.class, () -> restaurantUsecase.update(1L, dto));
    }

    @Test
    void delete_ShouldCallRepository() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        
        restaurantUsecase.delete(1L);

        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantException.class, () -> restaurantUsecase.delete(1L));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurantEntity));
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(restaurantResponse);

        List<RestaurantResponseDTO> result = restaurantUsecase.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findById_ShouldReturnRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantMapper.toResponseDTO(restaurantEntity)).thenReturn(restaurantResponse);

        RestaurantResponseDTO result = restaurantUsecase.findById(1L);

        assertNotNull(result);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantException.class, () -> restaurantUsecase.findById(1L));
    }
}
