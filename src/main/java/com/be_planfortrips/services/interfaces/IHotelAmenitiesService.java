package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.HotelAmenitiesDto;
import com.be_planfortrips.dto.response.HotelAmenitiesResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IHotelAmenitiesService {
    List<HotelAmenitiesResponse> getByHotelId(Long hotelId, String status);
    HotelAmenitiesResponse create(HotelAmenitiesDto hotelAmenitiesDto);
    HotelAmenitiesResponse update(Long id, HotelAmenitiesDto hotelAmenitiesDto);
    void delete(Long hotelAmenitiesId);
    void changeStatus(Long hotelAmenitiesId);
    void uploadIcon(Long hotelAmenitiesId, MultipartFile icon);
    void deleteIcon(Long hotelAmenitiesId);
}
