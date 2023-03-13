package com.congestion.calculator.controller;

import com.congestion.calculator.model.CalculateTaxRequest;
import com.congestion.calculator.service.TaxCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tax-calculator")
public class TaxCalculatorController {
    private final TaxCalculatorService taxCalculatorService;

    @Autowired
    public TaxCalculatorController(TaxCalculatorService taxCalculatorService) {
        this.taxCalculatorService = taxCalculatorService;
    }

    @PostMapping("/")
    public Double calculateTax(@RequestBody CalculateTaxRequest calculateTaxRequest) {
        return taxCalculatorService.calculateTax(calculateTaxRequest);
    }
}
