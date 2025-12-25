package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.request.CreateUserRequest;
import com.hnaya.inventra.dto.request.UpdateUserRequest;
import com.hnaya.inventra.dto.response.UserResponse;
import com.hnaya.inventra.entity.User;
import com.hnaya.inventra.entity.Warehouse;
import com.hnaya.inventra.entity.enums.Role;
import com.hnaya.inventra.exception.DuplicateResourceException;
import com.hnaya.inventra.exception.ResourceNotFoundException;
import com.hnaya.inventra.mapper.UserMapper;
import com.hnaya.inventra.repository.UserRepository;
import com.hnaya.inventra.repository.WarehouseRepository;
import com.hnaya.inventra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createManager(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(Warehouse.class ,request.getWarehouseId()));

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.MANAGER);
        user.setActive(true);
        user.setAssignedWarehouse(warehouse);

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class ,id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(User.class,username));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllManagers(Pageable pageable) {
        return userRepository.findByRole(Role.MANAGER, pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class,id));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        userMapper.updateEntityFromRequest(request, user);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException(Warehouse.class,+ request.getWarehouseId()));
            user.setAssignedWarehouse(warehouse);
        }

        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(User.class,id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void activate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, id));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public UserResponse assignWarehouse(Long userId, Long warehouseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, userId));

        if (user.getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("Only managers can be assigned to warehouses");
        }

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + warehouseId));

        if (userRepository.existsByAssignedWarehouseIdAndIdNot(warehouseId, userId)) {
            throw new IllegalArgumentException("Warehouse already has an assigned manager");
        }

        user.setAssignedWarehouse(warehouse);
        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    @Override
    public UserResponse unassignWarehouse(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, userId));

        if (user.getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("Only managers can be unassigned from warehouses");
        }

        user.setAssignedWarehouse(null);
        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }
}