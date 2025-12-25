package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.request.ProductRequestDTO;
import com.hnaya.inventra.dto.response.ProductResponseDTO;
import com.hnaya.inventra.entity.Product;
import com.hnaya.inventra.mapper.ProductMapper;
import com.hnaya.inventra.repository.ProductRepository;
import com.hnaya.inventra.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = productMapper.toEntity(dto);
        product.setMarge(product.getPrixVente() - product.getPrixAchat());
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Produit non trouvé");
        }
        productRepository.deleteById(id);
    }
}