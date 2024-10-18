package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.CouponDto;
import com.be_planfortrips.dto.response.CouponResponse;
import com.be_planfortrips.entity.Coupon;
import com.be_planfortrips.entity.DiscountType;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
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
    public CouponResponse createCoupon(CouponDto couponDto) throws Exception {
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
        if(coupon.getDiscountType().equals(DiscountType.PERCENT)){
            int percent = Integer.parseInt(String.valueOf(coupon.getDiscountValue()));
            if(percent<0 || percent>100){
                throw new AppException(ErrorType.percentIsUnprocessed);
            }
        }
        couponRepository.saveAndFlush(coupon);
        return couponMapper.toResponse(coupon);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Integer id, CouponDto couponDto) throws Exception {
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
        if(existingCoupon.getDiscountType().equals(DiscountType.PERCENT)){
            int percent = Integer.parseInt(String.valueOf(existingCoupon.getDiscountValue()));
            if(percent<0 || percent>100){
                throw new AppException(ErrorType.percentIsUnprocessed);
            }
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
    @Transactional
    public void deleteCouponById(Integer id) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        coupon.ifPresent(couponRepository::delete);
    }
}
