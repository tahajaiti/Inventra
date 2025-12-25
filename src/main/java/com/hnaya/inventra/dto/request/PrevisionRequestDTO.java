package com.hnaya.inventra.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requête pour générer une prévision.
 * Utilisé par l'endpoint POST /api/previsions/generer
 * 
 * @author Kawtar Shaimi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrevisionRequestDTO {

    /**
     * ID du produit pour lequel générer la prévision
     */
    @NotNull(message = "L'ID du produit est obligatoire")
    @Positive(message = "L'ID du produit doit être positif")
    private Long productId;

    /**
     * ID de l'entrepôt concerné
     */
    @NotNull(message = "L'ID de l'entrepôt est obligatoire")
    @Positive(message = "L'ID de l'entrepôt doit être positif")
    private Long warehouseId;
}
