package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.CreateWarehouseRequest;
import com.hnaya.inventra.dto.response.WarehouseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {

    WarehouseResponse create(CreateWarehouseRequest request);

    WarehouseResponse getById(Long id);

    Page<WarehouseResponse> getAll(Pageable pageable);

    WarehouseResponse update(Long id, CreateWarehouseRequest request);

    void delete(Long id);
}