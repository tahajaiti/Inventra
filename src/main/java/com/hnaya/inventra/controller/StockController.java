package com.hnaya.inventra.controller;

import com.hnaya.inventra.dto.request.StockRequestDTO;
import com.hnaya.inventra.dto.response.StockResponseDTO;
import com.hnaya.inventra.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // Généralement utilisé par l'ADMIN lors de l'approvisionnement initial.

    @PostMapping
    public ResponseEntity<StockResponseDTO> createStock(@Valid @RequestBody StockRequestDTO requestDTO) {
        return new ResponseEntity<>(stockService.save(requestDTO), HttpStatus.CREATED);
    }


     // Cest l'action principale du GESTIONNAIRE d'entrepôt.

    @PatchMapping("/{id}/quantite")
    public ResponseEntity<StockResponseDTO> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer nouvelleQuantite) {
        return ResponseEntity.ok(stockService.updateStockQuantity(id, nouvelleQuantite));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDTO> getStockById(@PathVariable Long id) {
        // Note: Assurez-vous d'ajouter findById dans votre interface/implémentation si nécessaire
        return ResponseEntity.ok(stockService.findById(id));
    }
}