package com.congestion.calculator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "toll_fees")
@Data
public class TollFees implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
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

    @Override
    public String toString() {
        return "TollFees{" +
                "id=" + id +
                ", city=" + city +
                ", fromHour=" + fromHour +
                ", fromMinute=" + fromMinute +
                ", toHour=" + toHour +
                ", toMinute=" + toMinute +
                ", rate=" + rate +
                '}';
    }
}
