package com.be_planfortrips.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDataByDate {

    List<RoomResponse> rooms;

    ScheduleResponse scheduleDes;
    ScheduleResponse scheduleOrigin;
    List<ScheduleSeatResponse> scheduleSeatsDes;
    List<ScheduleSeatResponse> scheduleSeatsOrigin;
}
