package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.response.PrevisionResponseDTO;
import com.hnaya.inventra.security.UserPrincipal;

import java.util.List;

public interface PrevisionService {

    PrevisionResponseDTO genererPrevision(Long productId, Long warehouseId, UserPrincipal principal);

    List<PrevisionResponseDTO> getPrevisionsByWarehouse(Long warehouseId, UserPrincipal principal);

    PrevisionResponseDTO getPrevisionById(Long id, UserPrincipal principal);
}

