package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {
    Vehicle findByCode(String code);
}
