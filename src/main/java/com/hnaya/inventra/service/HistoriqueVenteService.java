package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.HistoriqueVenteRequestDTO;
import com.hnaya.inventra.dto.response.HistoriqueVenteResponseDTO;

import java.util.List;

public interface HistoriqueVenteService {
    HistoriqueVenteResponseDTO enregistrerVente(HistoriqueVenteRequestDTO dto);
    List<HistoriqueVenteResponseDTO> findByWarehouse(Long warehouseId);
    List<HistoriqueVenteResponseDTO> findAll();
}
