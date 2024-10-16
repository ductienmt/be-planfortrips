package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.HotelImageDto;
import com.be_planfortrips.dto.response.HotelImageResponse;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.dto.response.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IHotelService {
    HotelResponse createHotel(HotelDto hotelDto) throws Exception;
    HotelResponse updateHotel(Long id,HotelDto hotelDto) throws Exception;
    Page<HotelResponse> getHotels(PageRequest request);
    HotelResponse getByHotelId(Long id) throws Exception;
    void deleteHotelById(Long id);
    HotelImageResponse createHotelImage(Long id, HotelImageDto hotelImageDto) throws Exception;
    Map<String, Object> getRoomAvailable(Integer numberPeople, LocalDateTime checkIn, LocalDateTime checkOut);
}
