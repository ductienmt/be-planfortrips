package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {

    @Query(value = "SELECT COUNT(*) FROM schedule_seats ss JOIN seats s ON ss.seat_id = s.id JOIN vehicles v ON s.vehicle_code = v.code WHERE ss.status = 'Empty' AND v.code = :vehicleCode", nativeQuery = true)
    long countEmptySeatsByVehicleCode(@Param("vehicleCode") String vehicleCode);

    @Query("SELECT s FROM Seat s WHERE s.vehicle.code = :vehicleCode")
    List<Seat> findByVehicleCode(@Param("vehicleCode") String vehicleCode);


}
