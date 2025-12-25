package com.hnaya.inventra.mapper;

import com.hnaya.inventra.dto.request.StockRequestDTO;
import com.hnaya.inventra.dto.response.StockResponseDTO;
import com.hnaya.inventra.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, WarehouseMapper.class})
public interface StockMapper {
    @Mapping(target = "warehouseId", source = "warehouse.id")
    StockResponseDTO toResponseDto(Stock stock);

    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "warehouse.id", source = "warehouseId")
    Stock toEntity(StockRequestDTO requestDto);
}
