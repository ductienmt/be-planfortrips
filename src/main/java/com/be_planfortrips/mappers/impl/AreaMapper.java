package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.AreaDto;
import com.be_planfortrips.dto.response.AreaResponse;
import com.be_planfortrips.entity.Area;
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
public class AreaMapper implements MapperInterface<AreaResponse, Area, AreaDto> {
    ModelMapper modelMapper;


    @Override
    public Area toEntity(AreaDto areaDto) {
        TypeMap<AreaDto, Area> typeMap = modelMapper.createTypeMap(areaDto, Area.class);
//        typeMap.addMappings(mapper -> mapper.skip(Area::setId));
        return modelMapper.map(areaDto, Area.class);
    }

    @Override
    public AreaResponse toResponse(Area area) {
        return this.modelMapper.map(area, AreaResponse.class);
    }

    @Override
    public void updateEntityFromDto(AreaDto areaDto, Area area) {
        TypeMap<AreaDto, Area> typeMap = modelMapper.createTypeMap(areaDto, Area.class);
        typeMap.addMappings(mapper -> mapper.skip(Area::setId));
        modelMapper.map(areaDto, area);
    }
}
