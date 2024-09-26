package com.be_planfortrips.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDto {
    private Integer flightId;
    private String flightName;
    private String flightCode;
    private Integer depatureAirport;
    private Integer arrivalAirport;
    private LocalDateTime depatureTime;
    private LocalDateTime arrivalTime;
    private Integer airplaneId;
}
