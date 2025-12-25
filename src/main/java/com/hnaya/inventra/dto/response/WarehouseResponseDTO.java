package com.hnaya.inventra.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarehouseResponseDTO {
    private Long id;
    private String name;
    private String city;
    private String address;
    private Integer maxCapacity;
    private LocalDateTime createdAt;
}