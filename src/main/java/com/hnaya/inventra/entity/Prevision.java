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
@Table(name = "previsions")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Prevision extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "date_prevision", nullable = false)
    private LocalDate datePrevision;

    @Column(name = "quantite_prevue_30j")
    private Double quantitePrevue30Jours;

    @Column(name = "niveau_confiance")
    private Double niveauConfiance;

    private String recommandation;
}
