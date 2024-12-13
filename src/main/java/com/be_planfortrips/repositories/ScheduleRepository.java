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

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.route.originStation.city.nameCity LIKE %:originalLocation% " +
            "AND s.route.destinationStation.city.nameCity LIKE %:destination% " +
            "AND s.departureTime BETWEEN :departureTime AND :returnTime")
    List<Schedule> findSchedulesByTimeAndLocations(
            @Param("departureTime") LocalDateTime departureTime,
            @Param("returnTime") LocalDateTime returnTime,
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
            + "AND CAST(s.departureTime AS DATE) = :departureDate ")
    List<Schedule> findSchedule(@Param("originalLocation") String originalLocation,
                                @Param("destination") String destination,
                                @Param("departureDate") LocalDate departureDate);

    List<Schedule> getSchedulesByRouteAndVehicleCode(Route route, Vehicle vehicle);
    List<Schedule> findByVehicleCodeIn(List<String> code);

    @Query(value = "SELECT COUNT(*) " +
            "FROM schedules " +
            "JOIN schedule_seats ON schedules.id = schedule_seats.schedule_id " +
            "WHERE schedule_seats.status = 'Empty' " +
            "AND schedules.id = :scheduleId", nativeQuery = true)
    Integer getNumberSeatsEmpty(@Param("scheduleId") Long scheduleId);

    @Query("select s from Schedule s where s.vehicleCode.carCompany.enterprise.accountEnterpriseId = :id")
    List<Schedule> getSchedulesByEnterpriseId(@Param("id") Long id);


    @Query("SELECT s FROM Schedule s " +
            "JOIN s.route r " +
            "JOIN r.originStation os " +
            "JOIN r.destinationStation ds " +
            "WHERE os.city.nameCity like %:originalLocation% " +
            "AND ds.city.nameCity like %:destination% " +
            "AND cast(s.departureTime as date) = :departureDate " +
            "AND s.price_for_one_seat BETWEEN :priceMin AND :priceMax")
    List<Schedule> getSchedulesSamePrice(@Param("priceMin") double priceMin,@Param("priceMax") double priceMax,
                                         @Param("originalLocation") String originalLocation, @Param("destination") String destination,
                                         @Param("departureDate") LocalDate departureDate);


    @Query(nativeQuery = true, value = "SELECT\n" +
            "    s.id AS seat_id,\n" +
            "    s.seat_number,\n" +
            "    ss.status AS status,\n" +
            "    sc.price_for_one_seat AS price\n" +
            "FROM\n" +
            "    schedule_seats ss\n" +
            "        JOIN\n" +
            "    seats s ON ss.seat_id = s.id\n" +
            "        JOIN\n" +
            "    schedules sc ON ss.schedule_id = sc.id\n" +
            "WHERE\n" +
            "    ss.schedule_id = :scheduleId;")
    List<Map<String, Object>> getSeatsByScheduleId(@Param("scheduleId") Integer scheduleId);
}
