package com.congestion.calculator.model;

import java.io.Serializable;

public record CityResponse(
        long id,
        String name)
        implements Serializable {
}
