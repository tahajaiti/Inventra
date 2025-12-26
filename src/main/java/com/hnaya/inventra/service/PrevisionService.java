package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.response.PrevisionResponseDTO;

import java.util.List;

public interface PrevisionService {

    PrevisionResponseDTO genererPrevision(Long productId, Long warehouseId);

    List<PrevisionResponseDTO> getPrevisionsByWarehouse(Long warehouseId);

    PrevisionResponseDTO getPrevisionById(Long id);
}
