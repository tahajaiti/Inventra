package com.hnaya.inventra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Warehouse extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String city;
    private String address;

    private Integer maxCapacity;
}
