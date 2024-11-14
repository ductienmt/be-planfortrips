package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomAmenitiesDto {
    String name;
    BigDecimal fee;
    String description;
    Boolean status;
    Integer roomId;
}