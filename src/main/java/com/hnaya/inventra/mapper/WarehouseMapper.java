package com.hnaya.inventra.mapper;

import com.hnaya.inventra.dto.request.WarehouseRequestDTO;
import com.hnaya.inventra.dto.response.WarehouseResponseDTO;
import com.hnaya.inventra.entity.Warehouse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    WarehouseResponseDTO toResponseDto(Warehouse warehouse);

    Warehouse toEntity(WarehouseRequestDTO requestDto);
}
