package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Vehicle;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatResponse {
    @JsonProperty("seat_id")
    Integer id;
//    Vehicle vehicle;
    @JsonProperty("seat_number")
    String seatNumber;
    String status;
}
