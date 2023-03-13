package com.congestion.calculator.model;

import java.io.Serializable;

public record TollFeesResponse(
        String cityName,
        int fromHour,
        int fromMinute,
        int toHour,
        int toMinute,
        double rate)
        implements Serializable {
}
