package com.hnaya.inventra.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HistoriqueVenteRequestDTO {
    @NotNull(message = "L'ID du produit est obligatoire")
    private Long productId;

    @NotNull(message = "L'ID de l'entrepôt est obligatoire")
    private Long warehouseId;

    @NotNull(message = "La date de vente est obligatoire")
    private LocalDate dateVente;

    @NotNull(message = "La quantité vendue est obligatoire")
    @Positive(message = "La quantité doit être supérieure à zéro")
    private Integer quantiteVendue;

    private String jourSemaine;
    private Integer mois;
    private Integer annee;
}
