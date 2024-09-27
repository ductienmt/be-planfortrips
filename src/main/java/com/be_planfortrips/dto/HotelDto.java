package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelDto {
    Integer enterpriseId;
    String address;
    String phoneNumber;
    String description;
    Integer rating;
    LocalDateTime createAt;
    Integer imageId;
}
