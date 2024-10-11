package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.CheckinDto;
import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.response.CheckinResponse;
import com.be_planfortrips.entity.Checkin;
import com.be_planfortrips.entity.CheckinImage;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CheckinMapper implements MapperInterface<CheckinResponse, Checkin, CheckinDto> {
    ModelMapper modelMapper;


    @Override
    public Checkin toEntity(CheckinDto checkinDto) {
        TypeMap<CheckinDto, Checkin> typeMap = modelMapper.createTypeMap(CheckinDto.class, Checkin.class);
        typeMap.addMappings(mapper -> mapper.skip(Checkin::setId));
        typeMap.addMappings(mapper -> mapper.skip(Checkin::setCity));
        return typeMap.map(checkinDto);
    }

    @Override
    public CheckinResponse toResponse(Checkin checkin) {

        return modelMapper.map(checkin, CheckinResponse.class);
    }

    @Override
    public void updateEntityFromDto(CheckinDto checkinDto, Checkin checkin) {
        modelMapper.map(checkinDto, checkin);
    }
}
