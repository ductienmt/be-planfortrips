package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class AirlineDto {
    private Integer airlineId;
    private String airlineName;
    private String airlineCode;
    private String airlineCountry;
    private Integer enterpriseId;
}
