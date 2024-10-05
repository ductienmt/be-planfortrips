package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.FlightDto;
import com.be_planfortrips.entity.Flight;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface IFlightService {
    List<FlightDto> getFlightsByCitiesAndDepartureTime(Timestamp departureTime, String departureCity, String arrivalCity);
}
