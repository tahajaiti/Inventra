package com.hnaya.inventra.dto.response;

import com.hnaya.inventra.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private String lastName;
    private String email;
    private Role role;
    private boolean active;
    private Long warehouseId;
    private String warehouseName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}