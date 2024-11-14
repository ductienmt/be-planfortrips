package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Route;
import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.entity.Vehicle;
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
    @Query("select s from Schedule s " +
            "where s.route.originStation.city.nameCity like %:originalLocation% " +
            "and s.route.destinationStation.city.nameCity like %:destination% " +
    "and cast(s.departureTime as localdate ) = :departureDate " +
    "and cast(s.departureTime as localtime ) >= :departureTime")
    List<Schedule> findSchedulesAfterSpecificTime(
            @Param("departureDate") LocalDate departureDate,
            @Param("departureTime") LocalTime departureTime,
            @Param("originalLocation") String originalLocation,
            @Param("destination") String destination
    );

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
            + "WHERE os.city.nameCity like %:originalLocation% "
            + "AND ds.city.nameCity like %:destination% "
            + "AND CAST(s.departureTime AS DATE) = :departureDate "
            + "AND CAST(s.arrivalTime AS DATE) >= :arrivalDate")
    List<Schedule> findSchedule(@Param("originalLocation") String originalLocation,
                                @Param("destination") String destination,
                                @Param("departureDate") LocalDate departureDate,
                                @Param("arrivalDate") LocalDate arrivalDate);

    List<Schedule> getSchedulesByRouteAndVehicleCode(Route route, Vehicle vehicle);
    List<Schedule> findByVehicleCodeIn(List<String> code);

}
