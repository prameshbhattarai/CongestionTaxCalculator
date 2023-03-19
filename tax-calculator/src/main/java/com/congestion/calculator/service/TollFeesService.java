package com.congestion.calculator.service;

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
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Slf4j
public class TollFeesService {

    private TollFeesRepository tollFeesRepository;
    private CityRepository cityRepository;
    private VehiclesRepository vehiclesRepository;

    @Autowired
    public TollFeesService(TollFeesRepository tollFeesRepository, CityRepository cityRepository, VehiclesRepository vehiclesRepository) {
        this.tollFeesRepository = tollFeesRepository;
        this.cityRepository = cityRepository;
        this.vehiclesRepository = vehiclesRepository;
    }

    public Collection<TollFeesResponse> getTollFeesByCityAndVehicle(Long cityId, String vehicleName) {
        var emptyResponse = new ArrayList<TollFeesResponse>();

        if (vehicleName != null && !vehicleName.isEmpty()) {
            var vehicle = vehiclesRepository.getVehicleByName(vehicleName);
            if (vehicle.isPresent() && vehicle.get().isTaxExempt()) {
                log.info(String.format("TollFeesService::getTollFeesByCity: %s is tax free.", vehicleName));
                return emptyResponse;
            }
        }

        var city = cityRepository.findById(cityId);
        if (city.isPresent()) {
            var cityName = city.get().getName();
            var tollFees = tollFeesRepository.getTollFeesByCity_Id(city.get().getId());
            if (tollFees.isEmpty()) {
                log.info(String.format("TollFeesService::getTollFeesByCity: Unable to find toll fees for city %s", cityName));
                return emptyResponse;
            }
            return mapToDto(tollFees);
        } else {
            log.info(String.format("TollFeesService::getTollFeesByCity: Unable to find city with id %s.", cityId));
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
        ).sorted(Comparator.comparingInt(TollFeesResponse::fromHour))
                .collect(Collectors.toList());
    }
}
