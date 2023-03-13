package com.congestion.calculator.service;

import com.congestion.calculator.exception.NotFoundException;
import com.congestion.calculator.model.CalculateTaxRequest;
import com.congestion.calculator.model.TollFeesResponse;
import com.congestion.calculator.repository.CityRepository;
import com.congestion.calculator.repository.TollFeesRepository;
import com.congestion.calculator.repository.VehiclesRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Slf4j
public class TaxCalculatorService {

    private TollFeesRepository tollFeesRepository;
    private CityRepository cityRepository;
    private VehiclesRepository vehiclesRepository;

    @Autowired
    public TaxCalculatorService(TollFeesRepository tollFeesRepository, CityRepository cityRepository, VehiclesRepository vehiclesRepository) {
        this.tollFeesRepository = tollFeesRepository;
        this.cityRepository = cityRepository;
        this.vehiclesRepository = vehiclesRepository;
    }

    public Double calculateTax(CalculateTaxRequest calculateTaxRequest) {
        var vehicleId = calculateTaxRequest.getVehicleId();
        var cityId = calculateTaxRequest.getCityId();
        var dates = calculateTaxRequest.getDates();

        var noTaxValue = 0.0;

        // check if vehicle is tax free or not
        if (vehicleId != null && vehicleId > 0) {
            var vehicle = vehiclesRepository.findById(vehicleId);
            if (vehicle.isEmpty()) {
                var errorMessage = String.format("Unable to find vehicle with id %s.", vehicleId);
                log.info(errorMessage);
                throw new NotFoundException(errorMessage);
            }
            if (vehicle.get().isTaxExempt()) {
                log.info(String.format("TaxCalculatorService::getTollFeesByCity: %s is tax free.", vehicle.get().getName()));
                return noTaxValue;
            }
        }

        // check if city exists or not
        var city = cityRepository.findById(cityId);
        if (city.isEmpty()) {
            var errorMessage = String.format("Unable to find city with id %s.", vehicleId);
            log.info(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        var cityName = city.get().getName();
        var tollFees = tollFeesRepository.getTollFeesByCity_Id(city.get().getId());
        if (tollFees.isEmpty()) {
            log.info(String.format("TaxCalculatorService::getTollFeesByCity: Unable to find toll fees for city %s", cityName));
            return noTaxValue;
        }

        // todo now calculate the tax..
        return null;
    }

    public Collection<TollFeesResponse> getTollFeesByCityAndVehicle(Long cityId, Long vehicleId) {
        var emptyResponse = new ArrayList<TollFeesResponse>();

        if (vehicleId != null && vehicleId > 0) {
            var vehicle = vehiclesRepository.findById(vehicleId);
            if (vehicle.isPresent() && vehicle.get().isTaxExempt()) {
                log.info(String.format("TaxCalculatorService::getTollFeesByCity: %s is tax free.", vehicle.get().getName()));
                return emptyResponse;
            }
        }

        var city = cityRepository.findById(cityId);
        if (city.isPresent()) {
            var cityName = city.get().getName();
            var tollFees = tollFeesRepository.getTollFeesByCity_Id(city.get().getId());
            if (tollFees.isEmpty()) {
                log.info(String.format("TaxCalculatorService::getTollFeesByCity: Unable to find toll fees for city %s", cityName));
                return emptyResponse;
            }
            return mapToDto(tollFees);
        } else {
            log.info(String.format("TaxCalculatorService::getTollFeesByCity: Unable to find city with id %s.", cityId));
        }
        return emptyResponse;
    }

    private Collection<TollFeesResponse> mapToDto(Collection<com.congestion.calculator.entity.TollFees> tollFees) {
        return tollFees.stream().map((tollFee) -> new TollFeesResponse(
                        tollFee.getCity().getName(),
                        tollFee.getFromHour(),
                        tollFee.getFromMinute(),
                        tollFee.getToHour(),
                        tollFee.getToMinute(),
                        tollFee.getRate()
                )
        ).collect(Collectors.toList());
    }
}
