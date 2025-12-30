package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.request.StockRequestDTO;
import com.hnaya.inventra.dto.response.StockResponseDTO;
import com.hnaya.inventra.entity.Stock;
import com.hnaya.inventra.exception.StockNotFoundException;
import com.hnaya.inventra.mapper.StockMapper;
import com.hnaya.inventra.repository.StockRepository;
import com.hnaya.inventra.service.StockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Transactional
    public StockResponseDTO updateStockQuantity(Long stockId, Integer newQuantity) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException("Stock non trouvé"));

        stock.setQuantiteDisponible(newQuantity);

        if (newQuantity <= stock.getSeuilAlerte()) {
            log.warn("ALERTE : Stock critique pour le produit {} (quantité: {}, seuil: {})",
                    stock.getProduct().getNom(),
                    newQuantity,
                    stock.getSeuilAlerte());
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
        return stockRepository.findById(id)
                .map(stockMapper::toResponseDto)
                .orElseThrow(() -> new StockNotFoundException("Stock non trouvé avec l'id : " + id));
    }
}
