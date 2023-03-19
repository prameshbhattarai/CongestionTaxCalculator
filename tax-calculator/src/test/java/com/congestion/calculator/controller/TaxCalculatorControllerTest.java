package com.congestion.calculator.controller;


import com.congestion.calculator.entity.City;
import com.congestion.calculator.entity.TollFees;
import com.congestion.calculator.entity.Vehicle;
import com.congestion.calculator.model.CalculateTaxRequest;
import com.congestion.calculator.repository.CityRepository;
import com.congestion.calculator.repository.HolidayRepository;
import com.congestion.calculator.repository.TollFeesRepository;
import com.congestion.calculator.repository.VehiclesRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaxCalculatorControllerTest {

    private static final String BASE_URL = "http://localhost";
    private static final String TAX_CALCULATOR_PATH = "/tax-calculator/";

    private static RestTemplate restTemplate;
    @LocalServerPort
    private int port;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private HolidayRepository holidayRepository;

    @MockBean
    private TollFeesRepository tollFeesRepository;

    @MockBean
    private VehiclesRepository vehiclesRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void calculateTax() {
        var CALCULATE_TAX_URL = BASE_URL.concat(":").concat(String.valueOf(port)).concat(TAX_CALCULATOR_PATH);
        var calculateTaxRequestHttpEntity = new HttpEntity<>(createCalculateTaxRequest());

        Mockito.when(vehiclesRepository.getVehicleByName(argThat(s -> s.equals("Car"))))
                .thenReturn(createVehicleEntity("Car"));

        Mockito.when(cityRepository.findById(argThat(s -> s.equals(1L))))
                .thenReturn(createCityEntity(1L));

        Mockito.when(tollFeesRepository.getTollFeesByCity_Id(argThat(s -> s.equals(1L))))
                .thenReturn(createTollFees());

        var result = restTemplate.exchange(CALCULATE_TAX_URL, HttpMethod.POST, calculateTaxRequestHttpEntity, Double.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(21, result.getBody());
    }

    private List<TollFees> createTollFees() {
        var rates = List.of(8, 13, 18, 13, 8, 13, 18, 13, 8, 0, 0);
        var fromHour = List.of(6, 6, 7, 8, 8, 15, 15, 17, 18, 18, 0);
        var fromMinute = List.of(0, 30, 0, 0, 30, 0, 30, 0, 0, 30, 0);
        var toHour = List.of(6, 6, 7, 8, 14, 15, 16, 17, 18, 23, 5);
        var toMinute = List.of(29, 59, 59, 29, 59, 29, 59, 59, 29, 0, 59);

        var tollFees = new ArrayList<TollFees>();

        for (int i = 0; i < 11; i++) {
            var fees = new TollFees();
            fees.setId(i + 1L);
            fees.setCity(createCityEntity(1L).get());
            fees.setFromHour(fromHour.get(i));
            fees.setFromMinute(fromMinute.get(i));
            fees.setToHour(toHour.get(i));
            fees.setToMinute(toMinute.get(i));
            fees.setRate(rates.get(i));
            tollFees.add(fees);
        }
        return tollFees;
    }

    private Optional<City> createCityEntity(long id) {
        var city = new City();
        city.setId(id);
        city.setName("Gothenborg");
        city.setMaxTaxableAmount(60.0);
        city.setSingleChargeTime(60);
        return Optional.of(city);
    }

    private Optional<Vehicle> createVehicleEntity(String vehicleName) {
        var vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setName(vehicleName);
        vehicle.setTaxExempt(false);
        return Optional.of(vehicle);
    }

    private CalculateTaxRequest createCalculateTaxRequest() {
        var calculateTaxRequest = new CalculateTaxRequest();
        calculateTaxRequest.setCityId(1L);
        calculateTaxRequest.setVehicleName("Car");
        calculateTaxRequest.setDates(List.of(
                parseDate("2013-01-14 21:00:00"),
                parseDate("2013-01-15 21:00:00"),
                parseDate("2013-02-07 06:23:27"),
                parseDate("2013-02-07 15:27:00")
        ));
        return calculateTaxRequest;
    }

    @SneakyThrows
    private Date parseDate(String dateString) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(dateString);
    }
}
