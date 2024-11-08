package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.RoomImage;
import com.be_planfortrips.entity.TypeOfRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {
    Integer id;
    String roomName;
    TypeOfRoom typeOfRoom;
    BigDecimal price;
    Integer rating;
    Integer maxSize;
    boolean isAvailable;
    @JsonIgnore
    Hotel hotel;

    String hotelName;
    List<RoomImage> images;
}
