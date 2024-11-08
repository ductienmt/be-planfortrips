package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.TypeOfRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseEnterprise {
    Integer id;
    String roomName;
    String typeOfRoom;
    BigDecimal price;
    Integer rating;
    Integer maxSize;
    boolean isAvailable;
}
