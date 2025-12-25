package com.hnaya.inventra.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class StockRequestDTO {
    @NotNull
    private Long productId;
    @NotNull private Long warehouseId;
    @NotNull @PositiveOrZero
    private Integer quantiteDisponible;
    @NotNull @PositiveOrZero
    private Integer seuilAlerte;
}