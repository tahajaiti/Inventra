package com.hnaya.inventra.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWarehouseRequest {

    @NotBlank(message = "Warehouse name is mandatory")
    private String name;

    @NotBlank(message = "City is mandatory")
    private String city;

    private String address;

    @NotNull(message = "Maximum capacity is mandatory")
    @Positive(message = "Capacity must be positive")
    private Integer maxCapacity;

}
