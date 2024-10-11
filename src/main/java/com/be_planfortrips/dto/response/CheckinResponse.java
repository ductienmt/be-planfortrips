package com.be_planfortrips.dto.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CheckinResponse {
    String cityName;
    String name;
    String address;
    BigDecimal latitude;
    BigDecimal longitude;
    BigDecimal payFee;
}
