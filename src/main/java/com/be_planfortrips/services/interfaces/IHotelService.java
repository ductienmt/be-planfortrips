package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.response.HotelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IHotelService {
    HotelResponse createHotel(HotelDto hotelDto) throws Exception;
    HotelResponse updateHotel(Long id,HotelDto hotelDto) throws Exception;
    Page<HotelResponse> getHotels(PageRequest request);
    HotelResponse getByHotelId(Long id) throws Exception;
    void deleteHotelById(Long id);
    HotelResponse createHotelImage(Long hotelId,  List<MultipartFile>  file) throws Exception ;
    HotelResponse deleteImage(Long id, List<Integer> imageIds) throws Exception;
}
