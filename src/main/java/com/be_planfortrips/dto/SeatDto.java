package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class SeatDto {
    private Integer seatId;
    private Integer airplaneId;
    private String seatNumber;
    private String status;
    private Integer classId;
    private Integer flightId;
}
