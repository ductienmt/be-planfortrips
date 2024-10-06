package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.AirlineDto;
import com.be_planfortrips.entity.Airline;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.dto.response.AirlineResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AirlineMapper implements MapperInterface<AirlineResponse, Airline, AirlineDto> {
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public Airline toEntity(AirlineDto airlineDto) {
        TypeMap<AirlineDto, Airline> typeMap = modelMapper.createTypeMap(airlineDto, Airline.class);
        typeMap.addMappings(mapper -> mapper.skip(Airline::setId));
        return modelMapper.map(airlineDto,Airline.class);
    }

    @Override
    public AirlineResponse toResponse(Airline airline) {
        return modelMapper.map(airline,AirlineResponse.class);
    }

    @Override
    public void updateEntityFromDto(AirlineDto airlineDto, Airline airline) {
        modelMapper.map(airlineDto, airline);
    }
}
