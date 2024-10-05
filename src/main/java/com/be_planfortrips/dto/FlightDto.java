package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightDto {
    String flightName;
    String flightCode;
    Integer departureAirport;
    Integer arrivalAirport;
    Timestamp departureTime;
    Timestamp arrivalTime;
    Integer airplaneId;
}
