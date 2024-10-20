package com.be_planfortrips.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckinDto {
    String cityId;
    String name;
    String address;
    BigDecimal latitude;
    BigDecimal longitude;
    BigDecimal payFee;
}
