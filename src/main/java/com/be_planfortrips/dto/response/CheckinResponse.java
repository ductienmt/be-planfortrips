package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Image;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CheckinResponse {
    Long id;
    String cityName;
    String name;
    String address;
    BigDecimal latitude;
    BigDecimal longitude;
    BigDecimal payFee;
    List<Image> images;
}
