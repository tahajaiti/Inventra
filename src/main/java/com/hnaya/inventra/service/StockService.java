package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.StockRequestDTO;
import com.hnaya.inventra.dto.response.StockResponseDTO;
import com.hnaya.inventra.security.UserPrincipal;

import java.util.List;

public interface StockService {
    StockResponseDTO save(StockRequestDTO dto);
    StockResponseDTO findById(Long id, UserPrincipal principal);
    StockResponseDTO updateStockQuantity(Long stockId, Integer newQuantity, UserPrincipal principal);
    List<StockResponseDTO> findByWarehouse(Long warehouseId);
}

