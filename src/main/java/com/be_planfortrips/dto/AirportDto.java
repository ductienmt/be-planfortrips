package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirportDto {
    String airportName;
    String airportCode;
    String city;
    String address;
    String country;
}
