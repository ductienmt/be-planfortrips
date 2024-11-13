package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.CheckinDto;
import com.be_planfortrips.dto.response.CheckinResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.CityRepository;
import com.be_planfortrips.repositories.ImageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CheckinMapper implements MapperInterface<CheckinResponse, Checkin, CheckinDto> {
    ModelMapper modelMapper;
    CityRepository cityRepository;
    ImageRepository imageRepository;
    @Override
    public Checkin toEntity(CheckinDto checkinDto) {
        TypeMap<CheckinDto, Checkin> typeMap = modelMapper.getTypeMap(CheckinDto.class, Checkin.class);

        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(CheckinDto.class, Checkin.class);
            typeMap.addMappings(mapper -> mapper.skip(Checkin::setId));
        }
        Checkin checkin = modelMapper.map(checkinDto, Checkin.class);
        if (checkinDto.getCityId() != null) {
            Optional<City> city = cityRepository.findById(checkinDto.getCityId());
            city.ifPresent(checkin::setCity);
        }
        return checkin;
    }

    @Override
    public CheckinResponse toResponse(Checkin checkin) {
        CheckinResponse response = modelMapper.map(checkin, CheckinResponse.class);
        // Lấy cityId từ Checkin và tìm City
        Optional<City> cityOpt = cityRepository.findById(checkin.getCity().getId());
        if (cityOpt.isPresent()) {
            response.setId(Long.valueOf(checkin.getId()));
            response.setCityName(cityOpt.get().getNameCity());
        } else {
            response.setCityName("Unknown City");
        }


        if (checkin.getCheckinImages() != null) {
            List<Image> images = checkin.getCheckinImages().stream()
                    .map(CheckinImage::getImage)
                    .collect(Collectors.toList());
            response.setImages(images);
        }

        return response;
    }

    @Override
    public void updateEntityFromDto(CheckinDto checkinDto, Checkin checkin) {
        modelMapper.map(checkinDto, checkin);
    }
}