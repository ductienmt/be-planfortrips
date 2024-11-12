package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.ScheduleSeat;
import com.be_planfortrips.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Integer> {
    List<ScheduleSeat> findByScheduleId(Integer scheduleId);
    Optional<ScheduleSeat> findByScheduleIdAndSeatId(Integer scheduleId, Integer seatId);

    @Query("SELECT ss FROM ScheduleSeat ss WHERE ss.schedule.id = :scheduleId AND ss.status = 'Empty'")
    List<ScheduleSeat> findEmptySeatsByScheduleId(@Param("scheduleId") Integer scheduleId);

}

