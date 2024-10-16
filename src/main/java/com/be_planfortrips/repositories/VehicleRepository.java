package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,String> {
    Vehicle findByCode(String code);

}
