package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class FlightPriceDto {
    private Integer flightId;
    private Integer classId;
    private String discountType;
    private Double discountValue;
}
