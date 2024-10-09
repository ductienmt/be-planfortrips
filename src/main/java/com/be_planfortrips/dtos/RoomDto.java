package com.be_planfortrips.dto;

import com.be_planfortrips.entity.TypeOfRoom;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDto {

    Boolean isAvailable;
    Integer maxSize;
    BigDecimal price;
    Double rating;
    Long hotelId;
    String roomName;
    String description;
    TypeOfRoom typeOfRoom;

}
