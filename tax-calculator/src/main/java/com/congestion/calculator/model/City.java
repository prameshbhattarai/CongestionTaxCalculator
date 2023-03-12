package com.congestion.calculator.model;

import java.io.Serializable;

public record City(
        long id,
        String name)
        implements Serializable {
}
