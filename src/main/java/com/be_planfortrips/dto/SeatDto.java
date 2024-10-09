package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatDto {
     Integer airplaneId;
     String seatNumber;
     String status;
     Integer classId;
     Integer flightId;
}
