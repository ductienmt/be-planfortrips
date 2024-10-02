package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.HotelImageDto;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.HotelImage;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.mappers.impl.HotelImageMapper;
import com.be_planfortrips.mappers.impl.HotelMapper;
import com.be_planfortrips.repositories.EnterpriseRepository;
import com.be_planfortrips.repositories.HotelImageRepository;
import com.be_planfortrips.repositories.HotelRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.responses.HotelImageResponse;
import com.be_planfortrips.responses.HotelResponse;
import com.be_planfortrips.services.interfaces.IHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class HotelService implements IHotelService {
    HotelRepository hotelRepository;
    EnterpriseRepository enterpriseRepository;
    ImageRepository imageRepository;
    HotelImageRepository hotelImageRepository;
    HotelMapper hotelMapper;
    HotelImageMapper hotelImageMapper;
    @Override
    @Transactional
    public HotelResponse createHotel(HotelDto hotelDto) throws Exception {
        AccountEnterprise accountEnterprise = enterpriseRepository.findById(hotelDto.getEnterpriseId())
                .orElseThrow(()->new Exception("Not found"));
        Hotel hotel = hotelMapper.toEntity(hotelDto);
        hotel.setAccountEnterprise(accountEnterprise);
        hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse updateHotel(Long id, HotelDto hotelDto) throws Exception {
        Hotel existHotel = hotelRepository.findById(id)
                           .orElseThrow(()->new Exception("Not found"));
        AccountEnterprise accountEnterprise = enterpriseRepository.findById(hotelDto.getEnterpriseId())
                .orElseThrow(()->new Exception("Not found"));
        hotelMapper.updateEntityFromDto(hotelDto,existHotel);
        existHotel.setId(id);
        existHotel.setAccountEnterprise(accountEnterprise);
        hotelRepository.saveAndFlush(existHotel);
        return hotelMapper.toResponse(existHotel);
    }

    @Override
    public Page<HotelResponse> getAllHotel(PageRequest request) {
        return hotelRepository.findAll(request).map(hotel -> hotelMapper.toResponse(hotel));
    }

    @Override
    public HotelResponse getByHotelId(Long id) throws Exception {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(()->new Exception("Not found"));
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
}
