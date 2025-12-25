package com.hnaya.inventra.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Table(name = "entrepots")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Entrepot extends BaseEntity {

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "ville", nullable = false)
    private String ville;

    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name ="code_postal", nullable = false)
    private String codePostal;

    @Column(name = "capacite_maximale", nullable = false)
    private double capaciteMaximale;
}
