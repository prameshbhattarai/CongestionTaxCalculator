package com.congestion.calculator.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "city")
@NoArgsConstructor
@Data
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_taxable_amount", nullable = false)
    private Double maxTaxableAmount;

    @Column(name = "single_charge_time", nullable = false)
    private Integer singleChargeTime;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<TollFees> tollFees;
}
