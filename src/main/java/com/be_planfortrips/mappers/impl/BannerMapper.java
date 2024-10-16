package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.BannerDto;
import com.be_planfortrips.dto.response.BannerResponseAdmin;
import com.be_planfortrips.entity.Banner;
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
public class BannerMapper implements MapperInterface<BannerResponseAdmin, Banner, BannerDto> {
    ModelMapper modelMapper;

    @Override
    public Banner toEntity(BannerDto bannerDto) {
        TypeMap<BannerDto, Banner> typeMap = modelMapper.getTypeMap(BannerDto.class, Banner.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(BannerDto.class, Banner.class);
            typeMap.addMappings(mapper -> mapper.skip(Banner::setId));
        }
        return typeMap.map(bannerDto);
    }

    @Override
    public BannerResponseAdmin toResponse(Banner banner) {

        return modelMapper.map(banner, BannerResponseAdmin.class);
    }

    @Override
    public void updateEntityFromDto(BannerDto bannerDto, Banner banner) {

        modelMapper.map(bannerDto, banner);
    }
}
