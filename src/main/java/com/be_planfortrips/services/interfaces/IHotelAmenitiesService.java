package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.HotelAmenitiesDto;
import com.be_planfortrips.dto.response.HotelAmenitiesResponse;

public interface IHotelAmenities {
    HotelAmenitiesResponse getByHotelId(Long hotelId);
    HotelAmenitiesResponse create(HotelAmenitiesDto hotelAmenitiesDto);
    HotelAmenitiesResponse update(HotelAmenitiesDto hotelAmenitiesDto);
    void delete(Long hotelAmenitiesId);
    void changeStatus(Long hotelAmenitiesId);
}
