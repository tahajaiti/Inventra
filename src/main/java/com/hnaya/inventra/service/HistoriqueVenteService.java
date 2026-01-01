package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.HistoriqueVenteRequestDTO;
import com.hnaya.inventra.dto.response.HistoriqueVenteResponseDTO;
import com.hnaya.inventra.security.UserPrincipal;

import java.util.List;

public interface HistoriqueVenteService {
    HistoriqueVenteResponseDTO enregistrerVente(HistoriqueVenteRequestDTO dto, UserPrincipal principal);
    List<HistoriqueVenteResponseDTO> findByWarehouse(Long warehouseId, UserPrincipal principal);
    List<HistoriqueVenteResponseDTO> findAll();
}

