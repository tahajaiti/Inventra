package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.request.StockRequestDTO;
import com.hnaya.inventra.dto.response.StockResponseDTO;
import com.hnaya.inventra.entity.Product;
import com.hnaya.inventra.entity.Stock;
import com.hnaya.inventra.entity.Warehouse;
import com.hnaya.inventra.entity.enums.Role;
import com.hnaya.inventra.exception.StockNotFoundException;
import com.hnaya.inventra.mapper.StockMapper;
import com.hnaya.inventra.repository.StockRepository;
import com.hnaya.inventra.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockServiceImpl stockService;

    private Stock stock;
    private Product product;
    private Warehouse warehouse;
    private StockResponseDTO responseDTO;
    private UserPrincipal adminPrincipal;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .nom("Produit Test")
                .build();

        warehouse = Warehouse.builder()
                .id(1L)
                .name("Entrepot Test")
                .build();

        stock = Stock.builder()
                .id(10L)
                .product(product)
                .warehouse(warehouse)
                .quantiteDisponible(100)
                .seuilAlerte(20)
                .build();

        responseDTO = new StockResponseDTO();

        adminPrincipal = new UserPrincipal(
                1L, "admin", "password", "Admin", "User", "admin@test.com",
                Role.ADMIN, true, null
        );
    }

    @Test
    @DisplayName("Devrait mettre à jour la quantité et sauvegarder")
    void updateStockQuantity_ShouldUpdateAndSave() {
        // Given
        Long stockId = 10L;
        Integer newQty = 50;
        when(stockRepository.findById(stockId)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockMapper.toResponseDto(any(Stock.class))).thenReturn(responseDTO);

        // When
        StockResponseDTO result = stockService.updateStockQuantity(stockId, newQty, adminPrincipal);

        // Then
        assertNotNull(result);
        assertEquals(newQty, stock.getQuantiteDisponible());
        verify(stockRepository).save(stock);
    }

    @Test
    @DisplayName("Devrait logger une alerte si le seuil est atteint")
    void updateStockQuantity_WhenBelowThreshold_ShouldTriggerAlertLogic() {
        // Given
        Long stockId = 10L;
        Integer lowQty = 15; // Inférieur au seuil de 20
        when(stockRepository.findById(stockId)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockMapper.toResponseDto(any(Stock.class))).thenReturn(responseDTO);

        // When
        stockService.updateStockQuantity(stockId, lowQty, adminPrincipal);

        // Then
        assertEquals(lowQty, stock.getQuantiteDisponible());
        // Ici, on vérifie que le save a bien eu lieu malgré l'alerte
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    @DisplayName("Devrait lancer StockNotFoundException si l'ID n'existe pas")
    void updateStockQuantity_WhenIdNotFound_ShouldThrowException() {
        // Given
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(StockNotFoundException.class, () -> stockService.updateStockQuantity(1L, 50, adminPrincipal));
        verify(stockRepository, never()).save(any());
    }

    @Test
    @DisplayName("Devrait créer un nouveau stock via le mapping DTO")
    void save_ShouldMapAndSave() {
        // Given
        StockRequestDTO requestDTO = new StockRequestDTO();
        when(stockMapper.toEntity(requestDTO)).thenReturn(stock);
        when(stockRepository.save(stock)).thenReturn(stock);
        when(stockMapper.toResponseDto(stock)).thenReturn(responseDTO);

        // When
        StockResponseDTO result = stockService.save(requestDTO);

        // Then
        assertNotNull(result);
        verify(stockRepository).save(any(Stock.class));
    }
}