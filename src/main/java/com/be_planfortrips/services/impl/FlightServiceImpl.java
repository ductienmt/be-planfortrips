//package com.be_planfortrips.services.impl;
//
//import com.be_planfortrips.dto.FlightDto;
//import com.be_planfortrips.entity.Flight;
//import com.be_planfortrips.mappers.impl.FlightMapper;
//import com.be_planfortrips.repositories.FlightRepository;
//import com.be_planfortrips.services.interfaces.IFlightService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.sql.Timestamp;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class FlightServiceImpl implements IFlightService {
//
//    @Autowired
//    private FlightRepository flightRepository;
//
//
//    @Autowired
//    private FlightMapper flightMapper;
//
//
//    @Override
//    public List<FlightDto> getFlightsByCitiesAndDepartureTime(Timestamp departureTime, String departureCity, String arrivalCity) {
//        List<Flight> flights = flightRepository.findFlightsByCitiesAndDepartureTime(departureTime, departureCity, arrivalCity);
//        return flights.stream().map(flightMapper::toResponse).collect(Collectors.toList());
//    }
//
//
//}
