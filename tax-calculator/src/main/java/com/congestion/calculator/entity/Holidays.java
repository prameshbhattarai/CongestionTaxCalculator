package com.congestion.calculator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "holidays")
@Data
public class Holidays implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "day", nullable = false)
    private int day;

    @Override
    public String toString() {
        return "Holidays{" +
                "id=" + id +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
