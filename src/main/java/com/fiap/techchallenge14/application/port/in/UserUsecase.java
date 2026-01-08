package com.fiap.techchallenge14.application.port.in;

import com.fiap.techchallenge14.domain.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.UserResponseDTO;
import com.fiap.techchallenge14.domain.dto.UserUpdateRequestDTO;

import java.util.List;

public interface UserUsecase {
    UserResponseDTO save(UserCreateRequestDTO dto);
    UserResponseDTO update(Long id, UserUpdateRequestDTO dto);
    void delete(Long id);
    List<UserResponseDTO> findUsers(String name);
    void changePassword(Long id, String newPassword);
}
