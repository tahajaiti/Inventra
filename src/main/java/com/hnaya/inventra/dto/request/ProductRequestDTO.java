package com.hnaya.inventra.dto.request;

import com.hnaya.inventra.entity.enums.UniteMesure;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequestDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    private String description;
    private String categorie;
    @NotNull @Positive
    private Double prixVente;
    @NotNull @Positive private Double prixAchat;
    private Double poids;
    private UniteMesure unite;
}
