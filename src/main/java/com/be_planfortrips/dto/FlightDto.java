package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightDto {
    String flightName;
    String flightCode;
    Integer departureAirport;
    Integer arrivalAirport;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
    Integer airplaneId;
}
