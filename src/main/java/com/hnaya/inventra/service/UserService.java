package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.CreateUserRequest;
import com.hnaya.inventra.dto.request.UpdateUserRequest;
import com.hnaya.inventra.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse createManager(CreateUserRequest request);

    UserResponse getById(Long id);

    UserResponse getByUsername(String username);

    Page<UserResponse> getAll(Pageable pageable);

    Page<UserResponse> getAllManagers(Pageable pageable);

    UserResponse update(Long id, UpdateUserRequest request);

    void delete(Long id);

    void activate(Long id);

    void deactivate(Long id);

    UserResponse assignWarehouse(Long userId, Long warehouseId);

    UserResponse unassignWarehouse(Long userId);
}