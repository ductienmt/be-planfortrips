package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Status;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleSeatResponse {

    Integer seatId;
    String seatNumber;
}
