package com.hnaya.inventra.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrevisionRequestDTO {

    @NotNull(message = "L'ID du produit est obligatoire")
    @Positive(message = "L'ID du produit doit être positif")
    private Long productId;

    @NotNull(message = "L'ID de l'entrepôt est obligatoire")
    @Positive(message = "L'ID de l'entrepôt doit être positif")
    private Long warehouseId;
}
