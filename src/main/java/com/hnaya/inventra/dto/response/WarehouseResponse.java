package com.hnaya.inventra.dto.response;

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
public class WarehouseResponse {
    private Long id;
    private String name;
    private String city;
    private String address;
    private Integer maxCapacity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}