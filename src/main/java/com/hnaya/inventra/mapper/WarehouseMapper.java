package com.hnaya.inventra.mapper;

import com.hnaya.inventra.dto.request.CreateWarehouseRequest;
import com.hnaya.inventra.dto.response.WarehouseResponse;
import com.hnaya.inventra.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    Warehouse toEntity(CreateWarehouseRequest request);

    WarehouseResponse toResponse(Warehouse warehouse);

    void updateEntityFromRequest(CreateWarehouseRequest request, @MappingTarget Warehouse warehouse);
}