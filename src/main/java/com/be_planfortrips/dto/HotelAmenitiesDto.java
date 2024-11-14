package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelAmenitiesDto {
    String name;
    BigDecimal fee;
    String description;
    Boolean status;
    Long hotelId;
}