package com.hnaya.inventra.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HistoriqueVenteResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private LocalDate dateVente;
    private Integer quantiteVendue;
    private String jourSemaine;
    private Integer mois;
    private Integer annee;
}
