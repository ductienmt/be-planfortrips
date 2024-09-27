package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDto {
    String roomName;
    Double price;
    Integer hotelId;
    String typeOfRoom;
    String description;
    Integer maxSize;
    Integer imageId;
    Integer rating;
    Boolean isAvailable;
}
