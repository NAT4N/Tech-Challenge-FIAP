package com.fiap.techchallenge14.application.port.in;

import com.fiap.techchallenge14.domain.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantUpdateRequestDTO;

import java.util.List;

public interface RestaurantUsecase {
    RestaurantResponseDTO save(RestaurantCreateRequestDTO dto);
    RestaurantResponseDTO update(Long id, RestaurantUpdateRequestDTO dto);
    void delete(Long id);
    List<RestaurantResponseDTO> findAll();
    RestaurantResponseDTO findById(Long id);
}
