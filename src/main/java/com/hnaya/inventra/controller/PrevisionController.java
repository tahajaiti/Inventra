package com.hnaya.inventra.controller;

import com.hnaya.inventra.dto.request.PrevisionRequestDTO;
import com.hnaya.inventra.dto.response.PrevisionResponseDTO;
import com.hnaya.inventra.service.PrevisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/previsions")
@RequiredArgsConstructor
public class PrevisionController {

    private final PrevisionService previsionService;

    @PostMapping("/generer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PrevisionResponseDTO> genererPrevision(@Valid @RequestBody PrevisionRequestDTO request) {
        PrevisionResponseDTO response = previsionService.genererPrevision(request.getProductId(),
                request.getWarehouseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/entrepot/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<PrevisionResponseDTO>> getPrevisionsByWarehouse(@PathVariable Long warehouseId) {
        List<PrevisionResponseDTO> previsions = previsionService.getPrevisionsByWarehouse(warehouseId);
        return ResponseEntity.ok(previsions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PrevisionResponseDTO> getPrevisionById(@PathVariable Long id) {
        PrevisionResponseDTO prevision = previsionService.getPrevisionById(id);
        return ResponseEntity.ok(prevision);
    }
}
