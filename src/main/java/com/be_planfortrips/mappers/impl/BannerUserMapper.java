package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.BannerDto;
import com.be_planfortrips.dto.response.BannerResponseUser;
import com.be_planfortrips.entity.Banner;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BannerUserMapper implements MapperInterface<BannerResponseUser, Banner, BannerDto> {
    ModelMapper modelMapper;
    @Override
    public Banner toEntity(BannerDto bannerDto) {
        return null;
    }

    @Override
    public BannerResponseUser toResponse(Banner banner) {
        BannerResponseUser response = modelMapper.map(banner, BannerResponseUser.class);
        return response;
    }

    @Override
    public void updateEntityFromDto(BannerDto bannerDto, Banner banner) {

    }
}
