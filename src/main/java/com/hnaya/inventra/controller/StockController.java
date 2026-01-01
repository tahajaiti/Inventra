package com.hnaya.inventra.controller;

import com.hnaya.inventra.dto.request.StockRequestDTO;
import com.hnaya.inventra.dto.response.StockResponseDTO;
import com.hnaya.inventra.security.UserPrincipal;
import com.hnaya.inventra.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // Généralement utilisé par l'ADMIN lors de l'approvisionnement initial.
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockResponseDTO> createStock(@Valid @RequestBody StockRequestDTO requestDTO) {
        return new ResponseEntity<>(stockService.save(requestDTO), HttpStatus.CREATED);
    }


     // Cest l'action principale du GESTIONNAIRE d'entrepôt.

    @PatchMapping("/{id}/quantite")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<StockResponseDTO> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer nouvelleQuantite,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(stockService.updateStockQuantity(id, nouvelleQuantite, principal));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<StockResponseDTO> getStockById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(stockService.findById(id, principal));
    }

    @GetMapping("/my-warehouse")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<StockResponseDTO>> getMyWarehouseStocks(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(stockService.findByWarehouse(principal.getWarehouseId()));
    }

    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponseDTO>> getStocksByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(stockService.findByWarehouse(warehouseId));
    }
}