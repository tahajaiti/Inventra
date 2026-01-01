package com.hnaya.inventra.controller;

import com.hnaya.inventra.dto.request.HistoriqueVenteRequestDTO;
import com.hnaya.inventra.dto.response.HistoriqueVenteResponseDTO;
import com.hnaya.inventra.security.UserPrincipal;
import com.hnaya.inventra.service.HistoriqueVenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class HistoriqueVenteController {

    private final HistoriqueVenteService historiqueVenteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<HistoriqueVenteResponseDTO> enregistrerVente(
            @Valid @RequestBody HistoriqueVenteRequestDTO requestDTO,
            @AuthenticationPrincipal UserPrincipal principal) {
        return new ResponseEntity<>(historiqueVenteService.enregistrerVente(requestDTO, principal), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistoriqueVenteResponseDTO>> getAllHistorique() {
        return ResponseEntity.ok(historiqueVenteService.findAll());
    }

    @GetMapping("/entrepot/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<HistoriqueVenteResponseDTO>> getHistoriqueByWarehouse(
            @PathVariable Long warehouseId,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(historiqueVenteService.findByWarehouse(warehouseId, principal));
    }

    @GetMapping("/my-warehouse")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<HistoriqueVenteResponseDTO>> getMyWarehouseHistorique(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(historiqueVenteService.findByWarehouse(principal.getWarehouseId(), principal));
    }
}