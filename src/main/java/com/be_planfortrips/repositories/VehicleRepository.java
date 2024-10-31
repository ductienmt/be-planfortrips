package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,String> {
    Vehicle findByCode(String code);
    @Query("select h from Vehicle h " +
            " where (:keyword is null or :keyword = '' or h.carCompany.name like %:keyword%)")
    Page<Vehicle> searchVehicles(Pageable pageable, @Param("keyword") String keyword);
}
