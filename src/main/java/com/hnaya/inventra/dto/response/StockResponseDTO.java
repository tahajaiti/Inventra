package com.hnaya.inventra.dto.response;

import lombok.Data;

@Data
public class StockResponseDTO {
    private Long id;
    private ProductResponseDTO product;
    private Long warehouseId;
    private Integer quantiteDisponible;
    private Integer seuilAlerte;
}