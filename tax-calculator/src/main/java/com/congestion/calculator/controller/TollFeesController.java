package com.congestion.calculator.controller;

import com.congestion.calculator.model.TollFeesResponse;
import com.congestion.calculator.service.TollFeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("toll-fees")
public class TollFeesController {
    private final TollFeesService tollFeesService;

    @Autowired
    public TollFeesController(TollFeesService tollFeesService) {
        this.tollFeesService = tollFeesService;
    }

    @GetMapping("/{cityId}")
    public Collection<TollFeesResponse> getTollFeesByCity(@PathVariable("cityId") Long cityId, @RequestParam(value = "vehicleName", required = false) String vehicleName) {
        return tollFeesService.getTollFeesByCityAndVehicle(cityId, vehicleName);
    }
}
