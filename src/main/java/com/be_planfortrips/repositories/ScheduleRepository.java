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
//    @Query(value = "SELECT s FROM Schedule s WHERE FUNCTION('DATE', s.departureTime) = :departureDate AND FUNCTION('TIME', s.departureTime) > :departureTime " +
//            "AND FUNCTION('DATE', s.arrivalTime) = :arrivalDate AND FUNCTION('TIME', s.arrivalTime) > :arrivalTime", nativeQuery = true)
//    List<Schedule> findSchedulesAfterSpecificTime(
//            @Param("departureDate") LocalDate departureDate,
//            @Param("departureTime") LocalTime departureTime,
//            @Param("arrivalDate") LocalDate arrivalDate,
//            @Param("arrivalTime") LocalTime arrivalTime
//    ); need review and edit
}
