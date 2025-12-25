package com.hnaya.inventra.mapper;

import com.hnaya.inventra.dto.request.HistoriqueVenteRequestDTO;
import com.hnaya.inventra.dto.response.HistoriqueVenteResponseDTO;
import com.hnaya.inventra.entity.HistoriqueVente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoriqueVenteMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    HistoriqueVenteResponseDTO toResponseDto(HistoriqueVente historique);

    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "warehouse.id", source = "warehouseId")
    HistoriqueVente toEntity(HistoriqueVenteRequestDTO requestDto);
}
