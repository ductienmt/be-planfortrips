package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {
    Long id;
    String roomName;
    TypeOfRoom typeOfRoom;
    BigDecimal price;
    String description;

    Integer rating;
    Integer maxSize;
    boolean isAvailable;
    @JsonIgnore
    Hotel hotel;

    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;

    String hotelName;
    List<String> images;
    List<RoomAmenitiesResponse> roomAmenities;
}
