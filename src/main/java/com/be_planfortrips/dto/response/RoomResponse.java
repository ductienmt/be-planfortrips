package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.TypeOfRoom;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {

    Integer roomId;
    String roomName;
    TypeOfRoom typeOfRoom;
    BigDecimal price;
    boolean isAvailable;
    Hotel hotel;

}
