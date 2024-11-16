package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.TourDto;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.dto.response.rsTourResponse.TourScheduleBringData;
import com.be_planfortrips.dto.response.rsTourResponse.TourScheduleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ITourService {

    List<TourResponse> getAllTour(Integer page, Integer size);

    TourResponse getTourById(Long tourId);
    TourResponse createTour(TourDto tourDto);

    void removeTourById(Long tourId);

    TourResponse updateTourById(Long tourId, TourDto tourDto);

    // Get Schedule Available
    List<TourScheduleResponse> getScheduleAvailable(LocalDateTime day, String cityId);



}
