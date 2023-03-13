package com.congestion.calculator.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "holidays")
@NoArgsConstructor
@Data
public class Holidays implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "day", nullable = false)
    private int day;
}
