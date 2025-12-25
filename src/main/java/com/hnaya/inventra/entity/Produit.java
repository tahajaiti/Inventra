package com.hnaya.inventra.entity;

import com.hnaya.inventra.enums.UniteMesure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Table(name = "produits")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Produit extends BaseEntity{

    @Column(name = "nom", nullable = false)
    private String nom;

    private String description;

    private String categorie;

    @Column(name = "prix_vente", nullable = false)
    private Double prixVente;

    @Column(name = "prix_achat")
    private Double prixAchat;

    private Double marge;

    private Double poids;

    @Enumerated(EnumType.STRING)
    private UniteMesure unite;
}
