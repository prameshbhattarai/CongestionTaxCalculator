package com.congestion.calculator.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "vehicles")
@NoArgsConstructor
@Data
public class Vehicles implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_tax_exempt", nullable = false)
    private boolean isTaxExempt;

}
