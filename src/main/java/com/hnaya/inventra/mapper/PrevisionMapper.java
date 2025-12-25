package com.hnaya.inventra.mapper;

import com.hnaya.inventra.dto.response.PrevisionResponseDTO;
import com.hnaya.inventra.entity.Prevision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrevisionMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.nom", target = "productName")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.name", target = "warehouseName")
    PrevisionResponseDTO toResponseDTO(Prevision prevision);

    List<PrevisionResponseDTO> toResponseDTOList(List<Prevision> previsions);
}
