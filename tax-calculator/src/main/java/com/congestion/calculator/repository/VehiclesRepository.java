package com.congestion.calculator.repository;

import com.congestion.calculator.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicle, Long> {
    @Query(value = "select v from Vehicle v where upper(v.name) = upper(:name)")
    Optional<Vehicle> getVehicleByName(String name);
}
