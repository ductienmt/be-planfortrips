package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.HotelImageDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.HotelImageMapper;
import com.be_planfortrips.mappers.impl.HotelMapper;
import com.be_planfortrips.repositories.EnterpriseRepository;
import com.be_planfortrips.repositories.HotelImageRepository;
import com.be_planfortrips.repositories.HotelRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.dto.response.HotelImageResponse;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.services.interfaces.IHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelService implements IHotelService {

    HotelRepository hotelRepository;

    EnterpriseRepository enterpriseRepository;

    ImageRepository imageRepository;

    HotelImageRepository hotelImageRepository;

    HotelMapper hotelMapper;

    HotelImageMapper hotelImageMapper;

    RoomServiceImpl roomServiceImpl;

    @Override
    @Transactional
    public HotelResponse createHotel(HotelDto hotelDto) throws Exception {
        AccountEnterprise accountEnterprise = enterpriseRepository.findById(hotelDto.getEnterpriseId())
                .orElseThrow(() -> new Exception("Not found"));
        Hotel hotel = hotelMapper.toEntity(hotelDto);
        hotel.setAccountEnterprise(accountEnterprise);
        hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse updateHotel(Long id, HotelDto hotelDto) throws Exception {
        Hotel existHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new Exception("Not found"));
        AccountEnterprise accountEnterprise = enterpriseRepository.findById(hotelDto.getEnterpriseId())
                .orElseThrow(() -> new Exception("Not found"));
        hotelMapper.updateEntityFromDto(hotelDto, existHotel);
        existHotel.setId(id);
        existHotel.setAccountEnterprise(accountEnterprise);
        hotelRepository.saveAndFlush(existHotel);
        return hotelMapper.toResponse(existHotel);
    }

    @Override
    public Page<HotelResponse> getHotels(PageRequest request) {
        return hotelRepository.findAll(request).map(hotel -> hotelMapper.toResponse(hotel));
    }

    @Override
    public HotelResponse getByHotelId(Long id) throws Exception {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new Exception("Not found"));
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Optional<Hotel> orderOptional = hotelRepository.findById(id);
        orderOptional.ifPresent(hotelRepository::delete);
    }

    @Override
    @Transactional
    public HotelImageResponse createHotelImage(Long hotelId, HotelImageDto hotelImageDto) throws Exception {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new Exception("Not found"));

        Image image = new Image();
        image.setUrl(hotelImageDto.getImageUrl());
        imageRepository.save(image);

        HotelImage hotelImage = new HotelImage();
        hotelImage.setHotel(hotel);
        hotelImage.setImage(image);
        hotelImageRepository.saveAndFlush(hotelImage);

        return hotelImageMapper.toResponse(hotelImage);
    }

    @Override
    public Map<String, Object> getRoomAvailable(Integer numberPeople, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<RoomResponse> availableRooms = roomServiceImpl.getRoomAvailable(numberPeople, checkIn, checkOut);
        Map<String, Object> hotelMap = new HashMap<>();
        for (RoomResponse roomResponse : availableRooms) {
            HotelResponse hotelResponse;
            try {
                hotelResponse = this.getByHotelId(roomResponse.getHotel().getId());
            } catch (Exception e) {
                throw new AppException(ErrorType.notFound);
            }
            if (!hotelMap.containsKey(roomResponse.getHotel().getId())) {
                Map<String, Object> hotelInfo = new HashMap<>();
                hotelInfo.put("hotelId", roomResponse.getHotel().getId());
                hotelInfo.put("hotelName", hotelResponse.getName());
                hotelInfo.put("hotelAddress", hotelResponse.getAddress());
                hotelInfo.put("hotelPhonenumber", hotelResponse.getPhoneNumber());
                hotelInfo.put("rating", hotelResponse.getRating());
                hotelInfo.put("hotelImages", hotelResponse.getHotelImageResponses());
                hotelInfo.put("roomAvailable", new HashSet<RoomResponse>());
                hotelMap.put(hotelResponse.getName(), hotelInfo);
            }
            Map<String, Object> roomInfo = new HashMap<>();
            roomInfo.put("roomId", roomResponse.getId());
            roomInfo.put("roomName", roomResponse.getRoomName());
            roomInfo.put("roomType", roomResponse.getTypeOfRoom());
            roomInfo.put("price", roomResponse.getPrice());
            roomInfo.put("availability", roomResponse.isAvailable());

            Set<Map<String, Object>> roomResponses = (Set<Map<String, Object>>) ((Map<String, Object>) hotelMap.get(hotelResponse.getName())).get("roomAvailable");
            roomResponses.add(roomInfo);
        }
        return hotelMap;
    }


}
