package com.congestion.calculator.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "toll_fees")
@NoArgsConstructor
@Data
public class TollFees implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @Column(name = "from_hour", nullable = false)
    private int fromHour;

    @Column(name = "from_minute", nullable = false)
    private int fromMinute;

    @Column(name = "to_hour", nullable = false)
    private int toHour;

    @Column(name = "to_minute", nullable = false)
    private int toMinute;

    @Column(name = "rate", nullable = false)
    private double rate;
}
