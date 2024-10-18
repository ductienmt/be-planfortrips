package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.CouponDto;
import com.be_planfortrips.dto.response.CouponResponse;
import com.be_planfortrips.entity.Coupon;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CouponMapper implements MapperInterface<CouponResponse, Coupon, CouponDto> {
    ModelMapper modelMapper;
    @Override
    public Coupon toEntity(CouponDto couponDTO) {
        TypeMap<CouponDto, Coupon> typeMap = modelMapper.getTypeMap(CouponDto.class, Coupon.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(CouponDto.class, Coupon.class);
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
    public void updateEntityFromDto(CouponDto couponDTO, Coupon coupon) {

    }
}
