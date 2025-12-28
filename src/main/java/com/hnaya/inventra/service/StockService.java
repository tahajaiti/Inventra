package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.StockRequestDTO;
import com.hnaya.inventra.dto.response.StockResponseDTO;

public interface StockService {
    StockResponseDTO save(StockRequestDTO dto);
    StockResponseDTO findById(Long id);
    StockResponseDTO updateStockQuantity(Long stockId, Integer newQuantity);
}
