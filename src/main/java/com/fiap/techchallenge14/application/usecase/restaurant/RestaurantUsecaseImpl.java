package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.application.port.in.RestaurantUsecase;
import com.fiap.techchallenge14.application.port.out.RestaurantRepositoryPort;
import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.domain.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantUpdateRequestDTO;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.mapper.RestaurantMapper;
import com.fiap.techchallenge14.infrastructure.mapper.UserEntityMapper;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantUsecaseImpl implements RestaurantUsecase {

    private final RestaurantRepositoryPort restaurantRepository;
    private final UserRepositoryPort userRepository;
    private final RestaurantMapper restaurantMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public RestaurantResponseDTO save(RestaurantCreateRequestDTO dto) {
        User owner = userRepository.findById(dto.ownerId())
                .orElseThrow(() -> new RestaurantException("Proprietário não encontrado com o ID: " + dto.ownerId()));

        if (RoleType.CLIENT.equals(owner.getRole())) {
            owner.setRole(RoleType.RESTAURANT_OWNER);
            userRepository.save(owner);
        }

        RestaurantEntity restaurantEntity = restaurantMapper.toDomain(dto);
        restaurantEntity.setOwner(userMapper.toEntity(owner));

        RestaurantEntity savedRestaurantEntity = restaurantRepository.save(restaurantEntity);
        log.info("Restaurante criado com o ID: {}", savedRestaurantEntity.getId());

        return restaurantMapper.toResponseDTO(savedRestaurantEntity);
    }

    @Override
    @Transactional
    public RestaurantResponseDTO update(Long id, RestaurantUpdateRequestDTO dto) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantException("Restaurante não encontrado com o ID: " + id));

        restaurantMapper.updateDomainFromDto(dto, restaurantEntity);

        if (dto.ownerId() != null) {
            User owner = userRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new RestaurantException("Proprietário não encontrado com o ID: " + dto.ownerId()));


            User ownerEntity = new User();
            ownerEntity.setId(owner.getId());

            restaurantEntity.setOwner(userMapper.toEntity(ownerEntity));
        }

        RestaurantEntity updatedRestaurantEntity = restaurantRepository.save(restaurantEntity);
        log.info("Restaurante atualizado com o ID: {}", updatedRestaurantEntity.getId());

        return restaurantMapper.toResponseDTO(updatedRestaurantEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (restaurantRepository.findById(id).isEmpty()) {
            throw new RestaurantException("Restaurante não encontrado com o ID: " + id);
        }
        restaurantRepository.deleteById(id);
        log.info("Restaurante deletado com o ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponseDTO findById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantMapper::toResponseDTO)
                .orElseThrow(() -> new RestaurantException("Restaurante não encontrado com o ID: " + id));
    }
}
