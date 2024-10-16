package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.CouponDTO;
import com.be_planfortrips.dto.response.CarResponse;
import com.be_planfortrips.dto.response.CouponResponse;
import com.be_planfortrips.entity.CarCompany;
import com.be_planfortrips.entity.Coupon;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CouponMapper implements MapperInterface<CouponResponse, Coupon, CouponDTO> {
    ModelMapper modelMapper;
    @Override
    public Coupon toEntity(CouponDTO couponDTO) {
        TypeMap<CouponDTO, Coupon> typeMap = modelMapper.getTypeMap(CouponDTO.class, Coupon.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(CouponDTO.class, Coupon.class);
            typeMap.addMappings(mapper -> mapper.skip(Coupon::setId));
        }
        Coupon coupon = modelMapper.map(couponDTO, Coupon.class);
        return coupon;
    }

    @Override
    public CouponResponse toResponse(Coupon coupon) {
        return modelMapper.map(coupon, CouponResponse.class);
    }

    @Override
    public void updateEntityFromDto(CouponDTO couponDTO, Coupon coupon) {

    }
}
