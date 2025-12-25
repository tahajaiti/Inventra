package com.hnaya.inventra.controller;

import com.hnaya.inventra.dto.request.CreateUserRequest;
import com.hnaya.inventra.dto.request.UpdateUserRequest;
import com.hnaya.inventra.dto.response.UserResponse;
import com.hnaya.inventra.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @PostMapping("/managers")
    public ResponseEntity<UserResponse> createManager(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createManager(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
        UserResponse response = userService.getByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAll(Pageable pageable) {
        Page<UserResponse> response = userService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/managers")
    public ResponseEntity<Page<UserResponse>> getAllManagers(Pageable pageable) {
        Page<UserResponse> response = userService.getAllManagers(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        userService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        userService.deactivate(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/assign-warehouse/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> assignWarehouse(
            @PathVariable Long userId,
            @PathVariable Long warehouseId) {
        UserResponse response = userService.assignWarehouse(userId, warehouseId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}/unassign-warehouse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> unassignWarehouse(@PathVariable Long userId) {
        UserResponse response = userService.unassignWarehouse(userId);
        return ResponseEntity.ok(response);
    }
}