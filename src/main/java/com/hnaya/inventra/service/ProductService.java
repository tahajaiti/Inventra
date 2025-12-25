package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.ProductRequestDTO;
import com.hnaya.inventra.dto.response.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO create(ProductRequestDTO dto);
    List<ProductResponseDTO> findAll();
    void delete(Long id);
}
