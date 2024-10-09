//package com.be_planfortrips.repositories;
//
//import com.be_planfortrips.entity.Flight;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public interface FlightRepository extends JpaRepository<Flight, Long> {
//    @Query("SELECT f FROM Flight f " +
//            "JOIN f.departureAirport da " +
//            "JOIN f.arrivalAirport aa " +
//            "WHERE f.departureTime = :departureTime " +
//            "AND da.city = :departureCity " +
//            "AND aa.city = :arrivalCity")
//    List<Flight> findFlightsByCitiesAndDepartureTime(@Param("departureTime") Timestamp departureTime,
//                                                     @Param("departureCity") String departureCity,
//                                                     @Param("arrivalCity") String arrivalCity);
//}
//
