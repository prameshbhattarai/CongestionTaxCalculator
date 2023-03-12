package com.congestion.calculator.repository;

import com.congestion.calculator.entity.TollFees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TollFeesRepository extends JpaRepository<TollFees, Long> {
}
