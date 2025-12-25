package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.request.StockRequestDTO;
import com.hnaya.inventra.dto.response.StockResponseDTO;
import com.hnaya.inventra.entity.Stock;
import com.hnaya.inventra.exception.ResourceNotFoundException;
import com.hnaya.inventra.mapper.StockMapper;
import com.hnaya.inventra.repository.StockRepository;
import com.hnaya.inventra.service.StockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Transactional
    public StockResponseDTO updateStockQuantity(Long stockId, Integer newQuantity) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé"));

        stock.setQuantiteDisponible(newQuantity);

        if (newQuantity <= stock.getSeuilAlerte()) {
            System.out.println("ALERTE : Stock critique pour " + stock.getProduct().getNom());
        }

        return stockMapper.toResponseDto(stockRepository.save(stock));
    }

    @Override
    public StockResponseDTO save(StockRequestDTO dto) {
        Stock stock = stockMapper.toEntity(dto);
        return stockMapper.toResponseDto(stockRepository.save(stock));
    }

    @Override
    public StockResponseDTO findById(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé"));
        return stockMapper.toResponseDto(stock);
    }
}
