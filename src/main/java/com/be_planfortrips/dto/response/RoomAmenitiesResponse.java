package com.be_planfortrips.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoomAmenitiesResponse {
    Integer id;
    String name;
    BigDecimal fee;
    String description;
//    Boolean status;
    String icon;
}
