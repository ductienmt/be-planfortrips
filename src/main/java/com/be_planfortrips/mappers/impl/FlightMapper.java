//package com.be_planfortrips.mappers.impl;
//
//import com.be_planfortrips.dto.FlightDto;
//import com.be_planfortrips.entity.Flight;
//import com.be_planfortrips.mappers.MapperInterface;
//import org.springframework.stereotype.Component;
//
//@Component
//public class FlightMapper implements MapperInterface<FlightDto, Flight, FlightDto> {
//
//    @Override
//    public Flight toEntity(FlightDto flightDto) {
//        return null;
//    }
//
//    @Override
//    public FlightDto toResponse(Flight flight) {
//        return FlightDto.builder()
//                .flightName(flight.getFlightName())
//                .flightCode(flight.getFlightCode())
//                .departureTime(flight.getDepartureTime())
//                .arrivalTime(flight.getArrivalTime())
//                .departureAirport(flight.getDepartureAirport().getId().intValue())
//                .arrivalAirport(flight.getArrivalAirport().getId().intValue())
//                .build();
//    }
//
//    @Override
//    public void updateEntityFromDto(FlightDto flightDto, Flight flight) {
//
//    }
//}
