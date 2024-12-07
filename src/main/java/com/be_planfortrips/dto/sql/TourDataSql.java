package com.be_planfortrips.dto.sql;

import java.util.List;

public interface TourDataSql {

    // Getter cho các trường trong SQL
    String getSchedule1DepartureTime();
    String getSchedule2DepartureTime();
    Integer getSchedule1Id();
    String getRoute1Id();
    Integer  getSchedule2Id();
    String getRoute2Id();
    Long getHotelId();

    List<Integer> getScheduleSeatIds1();
    List<Integer> getScheduleSeatIds2();

    List<Long> getHotelRoomIds();
}
