package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.responses.HotelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IHotelService {
    HotelResponse createHotel(HotelDto hotelDto) throws Exception;
    HotelResponse updateHotel(Long id,HotelDto hotelDto) throws Exception;
    Page<HotelResponse> getAllHotel(PageRequest request);
    HotelResponse getByHotelId(Long id) throws Exception;
    void deleteHotelById(Long id);
}
