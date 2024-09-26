package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class AirplaneDto {
    private Integer airplaneId;
    private String model;
    private Integer seatCapacity;
    private Integer airlineId;
}
