package com.congestion.calculator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class CalculateTaxRequest {

    private String vehicleName;

    @NotNull(message = "City Id is required.")
    private Long cityId;

    @NotEmpty(message = "Dates is required.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<Date> dates;
}
