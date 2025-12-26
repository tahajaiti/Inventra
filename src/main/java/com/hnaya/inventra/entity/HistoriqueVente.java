package com.hnaya.inventra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@Table(name = "historique_ventes")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HistoriqueVente extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "date_vente", nullable = false)
    private LocalDate dateVente;

    @Column(name = "quantite_vendue", nullable = false)
    private Integer quantiteVendue;

    private String jourSemaine;
    private Integer mois;
    private Integer annee;
}
