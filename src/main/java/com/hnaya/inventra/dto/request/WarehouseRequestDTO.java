package com.hnaya.inventra.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WarehouseRequestDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    @NotBlank(message = "La ville est obligatoire")
    private String city;
    @NotBlank(message = "L'address est obligatoire")
    private String address;
    @Positive(message = "La capacité doit être positive")
    private Integer maxCapacity;
}
