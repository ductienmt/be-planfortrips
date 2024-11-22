package com.be_planfortrips.dto.response.rsTourResponse;


public interface TourScheduleBringData {
    Long getCarCompanyId();
    String getVehicleCode();
    String getRouteId();
    Long getScheduleId();
    Long getStationId();

    Integer countSeat();
}

