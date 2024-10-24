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
import java.util.Map;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
    @Query(value = "select * from schedules where CAST(departure_time AS DATE) = :departureDate" +
            " AND CAST(departure_time AS TIME) >= :departureTime", nativeQuery = true)
    List<Schedule> findSchedulesAfterSpecificTime(
            @Param("departureDate") LocalDate departureDate,
            @Param("departureTime") LocalTime departureTime
    );
//    need review and edit

    @Query("SELECT s.route.originStation.name AS departureStation, "
            + "s.route.destinationStation.name AS arrivalStation "
            + "FROM Schedule s "
            + "JOIN s.route r "
            + "JOIN r.originStation os "
            + "JOIN r.destinationStation ds "
            + "WHERE s.id = :scheduleId")
    Map<String, Object> findStationsByScheduleId(@Param("scheduleId") Long scheduleId);


    @Query("SELECT s FROM Schedule s "
            + "JOIN s.route r "
            + "JOIN r.originStation os "
            + "JOIN r.destinationStation ds "
            + "WHERE os.name = :originalLocation "
            + "AND ds.name = :destination "
            + "AND CAST(s.departureTime AS DATE) = :departureDate "
            + "AND CAST(s.arrivalTime AS DATE) >= :arrivalDate")
    List<Schedule> findSchedule(@Param("originalLocation") String originalLocation,
                                @Param("destination") String destination,
                                @Param("departureDate") LocalDate departureDate,
                                @Param("arrivalDate") LocalDate arrivalDate);


}
