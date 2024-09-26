package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class AirportDto {
    private Integer airportId;
    private String airportName;
    private String airportCode;
    private String city;
    private String address;
    private String country;
}
