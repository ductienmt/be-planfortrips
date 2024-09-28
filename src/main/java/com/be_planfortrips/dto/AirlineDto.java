package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirlineDto {
    String airlineName;
    String airlineCode;
    String airlineCountry;
    Integer enterpriseId;
}
