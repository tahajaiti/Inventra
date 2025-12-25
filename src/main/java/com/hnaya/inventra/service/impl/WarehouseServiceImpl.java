package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.request.CreateWarehouseRequest;
import com.hnaya.inventra.dto.response.WarehouseResponse;
import com.hnaya.inventra.entity.Warehouse;
import com.hnaya.inventra.exception.ResourceNotFoundException;
import com.hnaya.inventra.mapper.WarehouseMapper;
import com.hnaya.inventra.repository.UserRepository;
import com.hnaya.inventra.repository.WarehouseRepository;
import com.hnaya.inventra.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    public WarehouseResponse create(CreateWarehouseRequest request) {
        Warehouse warehouse = warehouseMapper.toEntity(request);
        Warehouse saved = warehouseRepository.save(warehouse);
        return warehouseMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getById(Long id) {
        if (id == null) {
            throw new ResourceNotFoundException("No warehouse assigned to this manager");
        }

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        return warehouseMapper.toResponse(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarehouseResponse> getAll(Pageable pageable) {
        return warehouseRepository.findAll(pageable)
                .map(warehouseMapper::toResponse);
    }

    @Override
    public WarehouseResponse update(Long id, CreateWarehouseRequest request) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        warehouseMapper.updateEntityFromRequest(request, warehouse);
        Warehouse updated = warehouseRepository.save(warehouse);
        return warehouseMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + id);
        }
        warehouseRepository.deleteById(id);
    }
}