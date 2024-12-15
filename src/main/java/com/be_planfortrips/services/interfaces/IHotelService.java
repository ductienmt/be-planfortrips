package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.dto.response.HotelResponses.AvailableHotels;
import com.be_planfortrips.dto.response.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IHotelService {
    HotelResponse createHotel(HotelDto hotelDto) throws Exception;
    HotelResponse updateHotel(Long id,HotelDto hotelDto) throws Exception;
    Page<HotelResponse> searchHotels(PageRequest request,String keyword,Integer rating);
    HotelResponse getByHotelId(Long id) throws Exception;
    HotelResponse getHotelByRoomId(Long id) throws Exception;
    void deleteHotelById(Long id);
    HotelResponse createHotelImage(Long hotelId,  List<MultipartFile>  file) throws Exception ;
    HotelResponse deleteImage(Long id, List<Integer> imageIds) throws Exception;
    Map<String, Object> getRoomAvailable(LocalDateTime checkIn, LocalDateTime checkOut, String destination);
    List<Map<String, Object>> getHotelDetail();
    List<HotelResponse> getByEnterpriseId(Long enterpriseId);
    List<HotelResponse> getByRouteId(String routeId);
    Page<HotelResponse> findHotelAvailable(PageRequest request, String keyword, LocalDateTime dateTime, Long days);
    List<Map<String, Object>> getHotelsSamePrice(double price, String destination);
    void changeStatus(Long id);
    Page<Map<String, Object>> searchEnterprise(String keyword, Pageable pageable);
}

