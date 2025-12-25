package com.hnaya.inventra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

public class HistoriqueVente extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
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
