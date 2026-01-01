package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.request.StockRequestDTO;
import com.hnaya.inventra.dto.response.StockResponseDTO;
import com.hnaya.inventra.entity.Stock;
import com.hnaya.inventra.entity.enums.Role;
import com.hnaya.inventra.exception.AccessDeniedException;
import com.hnaya.inventra.exception.StockNotFoundException;
import com.hnaya.inventra.mapper.StockMapper;
import com.hnaya.inventra.repository.StockRepository;
import com.hnaya.inventra.security.UserPrincipal;
import com.hnaya.inventra.service.StockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Transactional
    public StockResponseDTO updateStockQuantity(Long stockId, Integer newQuantity, UserPrincipal principal) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException("Stock non trouvé"));

        validateWarehouseAccess(stock, principal);

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
    public StockResponseDTO findById(Long id, UserPrincipal principal) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new StockNotFoundException("Stock non trouvé avec l'id : " + id));
        
        validateWarehouseAccess(stock, principal);
        
        return stockMapper.toResponseDto(stock);
    }

    @Override
    public List<StockResponseDTO> findByWarehouse(Long warehouseId) {
        return stockRepository.findByWarehouseId(warehouseId).stream()
                .map(stockMapper::toResponseDto)
                .toList();
    }

    private void validateWarehouseAccess(Stock stock, UserPrincipal principal) {
        if (principal.getRole() == Role.MANAGER) {
            Long userWarehouseId = principal.getWarehouseId();
            Long stockWarehouseId = stock.getWarehouse().getId();
            
            if (userWarehouseId == null || !userWarehouseId.equals(stockWarehouseId)) {
                throw new AccessDeniedException("Vous n'avez pas accès aux stocks de cet entrepôt");
            }
        }
    }
}

