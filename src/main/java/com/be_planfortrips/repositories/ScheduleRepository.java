package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
    @Query(value = "select * from schedules where CAST(departure_time AS DATE) = :departureDate" +
            " AND CAST(departure_time AS TIME) >= :departureTime", nativeQuery = true)
    List<Schedule> findSchedulesAfterSpecificTime(
            @Param("departureDate") LocalDate departureDate,
            @Param("departureTime") LocalTime departureTime
    );
//    need review and edit
}
