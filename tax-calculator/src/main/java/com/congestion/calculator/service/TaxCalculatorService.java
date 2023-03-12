package com.congestion.calculator.service;

import com.congestion.calculator.model.TollFees;
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

    public Collection<TollFees> getTollFeesByCityAndVehicle(Long cityId, Long vehicleId) {
        var emptyResponse = new ArrayList<TollFees>();

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

    private Collection<TollFees> mapToDto(Collection<com.congestion.calculator.entity.TollFees> tollFees) {
        return tollFees.stream().map((tollFee) -> new TollFees(
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
