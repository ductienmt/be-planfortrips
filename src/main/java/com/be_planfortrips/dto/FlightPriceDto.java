package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightPriceDto {
    Integer flightId;
    Integer classId;
    String discountType;
    BigDecimal discountValue;
}
