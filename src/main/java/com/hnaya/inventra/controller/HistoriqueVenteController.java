package com.hnaya.inventra.controller;

import com.hnaya.inventra.dto.request.HistoriqueVenteRequestDTO;
import com.hnaya.inventra.dto.response.HistoriqueVenteResponseDTO;
import com.hnaya.inventra.service.HistoriqueVenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class HistoriqueVenteController {

    private final HistoriqueVenteService historiqueVenteService;


    @PostMapping
    public ResponseEntity<HistoriqueVenteResponseDTO> enregistrerVente(
            @Valid @RequestBody HistoriqueVenteRequestDTO requestDTO) {
        return new ResponseEntity<>(historiqueVenteService.enregistrerVente(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HistoriqueVenteResponseDTO>> getAllHistorique() {
        return ResponseEntity.ok(historiqueVenteService.findAll());
    }


    @GetMapping("/entrepot/{warehouseId}")
    public ResponseEntity<List<HistoriqueVenteResponseDTO>> getHistoriqueByWarehouse(
            @PathVariable Long warehouseId) {
        return ResponseEntity.ok(historiqueVenteService.findByWarehouse(warehouseId));
    }
}