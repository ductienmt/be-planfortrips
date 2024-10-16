package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {

    @Query(value = "SELECT COUNT(*) FROM seats s JOIN vehicles v ON s.vehicle_code = v.code WHERE s.status = 'Empty' AND v.code = :vehicleCode", nativeQuery = true)
    long countEmptySeatsByVehicleCode(@Param("vehicleCode") String vehicleCode);


}
