package com.congestion.calculator.controller;

import com.congestion.calculator.model.TollFees;
import com.congestion.calculator.service.TaxCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("toll-fees")
public class TollFeesController {
    private final TaxCalculatorService taxCalculatorService;

    @Autowired
    public TollFeesController(TaxCalculatorService taxCalculatorService) {
        this.taxCalculatorService = taxCalculatorService;
    }

    @GetMapping("/{cityId}")
    public Collection<TollFees> getTollFeesByCity(@PathVariable("cityId") Long cityId, @RequestParam(value = "vehicleId", required = false) Long vehicleId) {
        return taxCalculatorService.getTollFeesByCityAndVehicle(cityId, vehicleId);
    }
}
