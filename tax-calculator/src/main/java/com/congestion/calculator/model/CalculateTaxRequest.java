package com.congestion.calculator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CalculateTaxRequest {

    private String vehicleName;

    @NotNull(message = "City Id is required.")
    private Long cityId;

    @NotEmpty(message = "Dates is required.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET", locale = "en_GB")
    private List<Date> dates;
}
