package com.congestion.calculator.service;

import com.congestion.calculator.entity.City;
import com.congestion.calculator.entity.Holidays;
import com.congestion.calculator.entity.TollFees;
import com.congestion.calculator.exception.NotFoundException;
import com.congestion.calculator.model.CalculateTaxRequest;
import com.congestion.calculator.repository.CityRepository;
import com.congestion.calculator.repository.HolidayRepository;
import com.congestion.calculator.repository.TollFeesRepository;
import com.congestion.calculator.repository.VehiclesRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@NoArgsConstructor
@Slf4j
public class TaxCalculatorService {

    private TollFeesRepository tollFeesRepository;
    private HolidayRepository holidayRepository;
    private CityRepository cityRepository;
    private VehiclesRepository vehiclesRepository;

    @Autowired
    public TaxCalculatorService(TollFeesRepository tollFeesRepository, HolidayRepository holidayRepository, CityRepository cityRepository, VehiclesRepository vehiclesRepository) {
        this.tollFeesRepository = tollFeesRepository;
        this.holidayRepository = holidayRepository;
        this.cityRepository = cityRepository;
        this.vehiclesRepository = vehiclesRepository;
    }

    public Double calculateTax(CalculateTaxRequest calculateTaxRequest) {
        var vehicleName = calculateTaxRequest.getVehicleName();
        var cityId = calculateTaxRequest.getCityId();
        var noTaxValue = 0.0;

        // check if vehicle is tax free or not
        var isVehicleTaxFree = isVehicleTaxFree(vehicleName);
        if (isVehicleTaxFree) return noTaxValue;

        // check if city exist of not
        var city = getCityById(cityId);
        var tollFees = tollFeesRepository.getTollFeesByCity_Id(city.getId());
        if (tollFees.isEmpty()) {
            log.info(String.format("TaxCalculatorService::getTollFeesByCity: Unable to find toll fees for city %s", city.getName()));
            return noTaxValue;
        }

        // sort the dates from request
        calculateTaxRequest.setDates(calculateTaxRequest.getDates().stream().sorted().toList());
        Map<String, List<Date>> eachDayEntries = mapEachDayEntries(calculateTaxRequest.getDates());
        return calculateTotalTax(eachDayEntries, city, tollFees);
    }

    private double calculateTotalTax(Map<String, List<Date>> eachDayEntries, City city, Collection<TollFees> tollFees) {
        System.out.println("city " + city);
        int totalTax = 0;
        for (Map.Entry<String, List<Date>> entry : eachDayEntries.entrySet()) {
            double tax = calculateEachDayTax(entry.getValue(), city, tollFees);
            System.out.println("each day tax " + entry.getKey() + "  value " + tax);
            totalTax += tax;
        }
        return totalTax;
    }

    private double calculateEachDayTax(List<Date> dates, City city, Collection<TollFees> tollFees) {
        if (dates.isEmpty()) return 0.0;

        double tax = 0;
        Date entryTime = dates.get(0);
        tax += getTollEntryFee(entryTime, tollFees);

        for (int i = 1; i < dates.size(); i++) {
            Date nextEntryTime = dates.get(i);
            // if nextEntryTime is less than SINGLE_CHARGE_TIME, then we will not add nexEntryTime charge
            // if nextEntryTime is greater than SINGLE_CHARGE_TIME, then only we will add tax
            long diffInMinutes = (nextEntryTime.getTime() - entryTime.getTime()) / 1000 / 60;
            if (diffInMinutes > city.getSingleChargeTime()) {
                tax += getTollEntryFee(nextEntryTime, tollFees);
                entryTime = nextEntryTime;
            }

            // if tax amount reach the max taxable amount per day
            // then return max taxable amount
            if (tax > city.getMaxTaxableAmount()) {
                return city.getMaxTaxableAmount();
            }
        }
        return tax;
    }

    public double getTollEntryFee(Date date, Collection<TollFees> tollFees) {
        if (isTollFreeDate(date)) return 0;

        int hour = date.getHours();
        int minute = date.getMinutes();

        for (TollFees tollFee : tollFees) {

            int fromHour = tollFee.getFromHour();
            int fromMinute = tollFee.getFromMinute();
            int toHour = tollFee.getToHour();
            int toMinute = tollFee.getToMinute();
            double rate = tollFee.getRate();

            Date fromDate = new Date();
            fromDate.setHours(fromHour);
            fromDate.setMinutes(fromMinute);

            Date toDate = new Date();
            toDate.setHours(toHour);
            toDate.setMinutes(toMinute);

            Date entryDate = new Date();
            entryDate.setHours(hour);
            entryDate.setMinutes(minute);

            // entry dates calculation are inclusive
            if ((entryDate.after(fromDate) || entryDate.equals(fromDate)) &&
                    (entryDate.before(toDate) || entryDate.equals(toDate))) {
                return rate;
            }
        }
        return 0;
    }

    private boolean isTollFreeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH) + 1; // MONTH starts from 0 i.e. January
        int day = calendar.get(Calendar.DAY_OF_WEEK); // DAY_OF_WEEK starts from 1 i.e. SUNDAY
        int dayOfMonth = calendar.get(Calendar.DATE);

        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
            return true;
        }

        Holidays holidays = holidayRepository.getHolidaysByMonthAndAndDay(month, dayOfMonth);
        return (holidays != null);
    }

    private Map<String, List<Date>> mapEachDayEntries(List<Date> dates) {
        var entryByDates = new HashMap<String, List<Date>>();

        dates.forEach((date) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            StringBuilder key = new StringBuilder().append(calendar.get(Calendar.YEAR)).append(calendar.get(Calendar.MONTH)).append(calendar.get(Calendar.DATE));
            List<Date> sameDate = entryByDates.getOrDefault(key.toString(), new ArrayList<>());
            sameDate.add(date);
            entryByDates.put(key.toString(), sameDate);
        });

        return entryByDates;
    }

    private City getCityById(long cityId) {
        var city = cityRepository.findById(cityId);
        if (city.isEmpty()) {
            var errorMessage = String.format("Unable to find city with id %s.", cityId);
            log.info(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return city.get();
    }

    private boolean isVehicleTaxFree(String vehicleName) {
        if (vehicleName != null && !vehicleName.isEmpty()) {
            var vehicle = vehiclesRepository.getVehicleByName(vehicleName);
            if (vehicle.isEmpty()) {
                var errorMessage = String.format("Unable to find vehicle with name %s.", vehicleName);
                log.info(errorMessage);
                throw new NotFoundException(errorMessage);
            }
            return vehicle.get().isTaxExempt();
        }
        return false;
    }
}
