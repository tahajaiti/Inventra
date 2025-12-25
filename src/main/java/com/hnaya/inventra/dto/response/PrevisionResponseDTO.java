package com.hnaya.inventra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrevisionResponseDTO {

    private Long id;

    private Long productId;

    private String productName;

    private Long warehouseId;

    private String warehouseName;

    private LocalDate datePrevision;

    private Double quantitePrevue30Jours;

    private Double niveauConfiance;

    private Integer quantiteACommander;

    private String recommandation;

    private Integer stockActuel;

    private Integer seuilAlerte;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
