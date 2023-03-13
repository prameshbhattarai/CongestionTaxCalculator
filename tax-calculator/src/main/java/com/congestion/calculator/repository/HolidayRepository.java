package com.congestion.calculator.repository;

import com.congestion.calculator.entity.Holidays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<Holidays, Long> {
}
