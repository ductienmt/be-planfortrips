package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.CouponDTO;
import com.be_planfortrips.dto.response.CouponResponse;
import com.be_planfortrips.entity.Coupon;
import com.be_planfortrips.mappers.impl.CouponMapper;
import com.be_planfortrips.repositories.CouponRepository;
import com.be_planfortrips.services.interfaces.ICouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CouponService implements ICouponService {
    CouponRepository couponRepository;
    CouponMapper couponMapper;
    @Scheduled(cron = "0 0 0 * * ?")
    public void deactivateExpiredCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        coupons.forEach(coupon -> {
            if (LocalDate.now().isAfter(coupon.getEndDate())) {
                coupon.setActive(false);
                couponRepository.save(coupon);
            }
        });
    }
    @Override
    @Transactional
    public CouponResponse createCoupon(CouponDTO couponDto) throws Exception {
        if(couponRepository.existsByCode(couponDto.getCode())){
            throw new Exception("Coupon is exist");
        }
        List<String> allowedDiscountTypes = Arrays.asList("PERCENT", "FIXED_AMOUNT");
        if(!allowedDiscountTypes.contains(couponDto.getDiscountType())){
            throw new Exception("The loai giam gia khong hop le");
        }
        Coupon coupon = couponMapper.toEntity(couponDto);
        if (coupon.getStartDate().isBefore(LocalDate.now())) {
            throw new Exception("Shopping date must be at least today!");
        }
        couponRepository.saveAndFlush(coupon);
        return couponMapper.toResponse(coupon);
    }

    @Override
    public CouponResponse updateCoupon(Integer id, CouponDTO couponDto) throws Exception {
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(()->new Exception("Coupon is not found"));
        existingCoupon = couponMapper.toEntity(couponDto);
        if (existingCoupon.getStartDate().isBefore(LocalDate.now())) {
            throw new Exception("Coupon start date must be at least today!");
        }
        existingCoupon.setId(id);
        List<String> allowedDiscountTypes = Arrays.asList("PERCENT", "FIXED_AMOUNT");
        if(!allowedDiscountTypes.contains(couponDto.getDiscountType())){
            throw new Exception("The loai giam gia khong hop le");
        }
        couponRepository.saveAndFlush(existingCoupon);
        return couponMapper.toResponse(existingCoupon);
    }

    @Override
    public Page<CouponResponse> getCoupons(PageRequest request) {
        return couponRepository.findAll(request).map(couponMapper::toResponse);
    }

    @Override
    public CouponResponse getByCouponId(Integer id) throws Exception {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if(!coupon.isPresent()){
            throw new Exception("Coupon is not exist");
        }
        return couponMapper.toResponse(coupon.get());
    }

    @Override
    public void deleteCouponById(Integer id) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        coupon.ifPresent(couponRepository::delete);
    }
}
