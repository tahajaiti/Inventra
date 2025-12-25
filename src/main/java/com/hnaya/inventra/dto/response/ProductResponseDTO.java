package com.hnaya.inventra.dto.response;

import com.hnaya.inventra.entity.enums.UniteMesure;
import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long id;
    private String nom;
    private String description;
    private String categorie;
    private Double prixVente;
    private Double poids;
    private UniteMesure unite;
}
