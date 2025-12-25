package com.hnaya.inventra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


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

    @Column(name = "max_capacity")
    private Integer maxCapacity;


    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<Stock> stocks;


    @OneToMany(mappedBy = "assignedWarehouse")
    private List<User> managers;

}
