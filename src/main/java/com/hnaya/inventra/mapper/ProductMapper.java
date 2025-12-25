package com.hnaya.inventra.mapper;

import com.hnaya.inventra.dto.request.ProductRequestDTO;
import com.hnaya.inventra.dto.response.ProductResponseDTO;
import com.hnaya.inventra.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDTO toResponseDto(Product product);

    Product toEntity(ProductRequestDTO requestDto);
}
