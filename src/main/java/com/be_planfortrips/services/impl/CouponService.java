package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.CouponDto;
import com.be_planfortrips.dto.response.CouponResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.CouponMapper;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.CouponRepository;
import com.be_planfortrips.repositories.CouponRoomRepository;
import com.be_planfortrips.repositories.RoomRepository;
import com.be_planfortrips.services.interfaces.ICouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponService implements ICouponService {
    CouponRepository couponRepository;
    CouponMapper couponMapper;
    AccountEnterpriseRepository enterpriseRepository;
    private final TokenMapperImpl tokenMapperImpl;
    private final RoomRepository roomRepository;
    private final CouponRoomRepository couponRoomRepository;

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
        if (couponRepository.existsByCode(couponDto.getCode())) {
            throw new Exception("Coupon is exist");
        }
        List<String> allowedDiscountTypes = Arrays.asList("PERCENT", "FIXED_AMOUNT");
        if (!allowedDiscountTypes.contains(couponDto.getDiscountType())) {
            throw new Exception("The loai giam gia khong hop le");
        }
        Coupon coupon = couponMapper.toEntity(couponDto);
        if (coupon.getStartDate().isBefore(LocalDate.now())) {
            throw new Exception("Shopping date must be at least today!");
        }
        if (coupon.getDiscountType().equals(DiscountType.PERCENT)) {
            BigDecimal discountValue = coupon.getDiscountValue();
            int percent = discountValue.intValue();
            if (percent < 0 || percent > 100) {
                throw new AppException(ErrorType.percentIsUnprocessed);
            }
        }
        coupon.setActive(couponDto.getIsActive());
        if (couponDto.getEnterpriseUsername() != null) {
            AccountEnterprise accountEnterprise = enterpriseRepository.findByUsername(couponDto.getEnterpriseUsername());
            if (accountEnterprise != null) {
                coupon.setAccountEnterprise(accountEnterprise);
            }
        }
        couponRepository.saveAndFlush(coupon);
        return couponMapper.toResponse(coupon);
    }

    @Override
    public CouponResponse createCouponRoom(CouponDto CouponDto, Long roomId) throws Exception {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new Exception("Room is not found"));
        if (couponRepository.existsByCode(CouponDto.getCode())) {
            throw new Exception("Mã giảm giá đã tồn tại");
        }
        List<String> allowedDiscountTypes = Arrays.asList("PERCENT", "FIXED_AMOUNT");
        if (!allowedDiscountTypes.contains(CouponDto.getDiscountType())) {
            throw new Exception("Thể loại giảm giá không hợp lệ");
        }
        Coupon coupon = couponMapper.toEntity(CouponDto);
        if (coupon.getDiscountType().equals(DiscountType.PERCENT)) {
            BigDecimal discountValue = coupon.getDiscountValue();
            int percent = discountValue.intValue();
            if (percent < 0 || percent > 100) {
                throw new AppException(ErrorType.percentIsUnprocessed);
            }
        }
        coupon.setActive(CouponDto.getIsActive());
        if (CouponDto.getEnterpriseUsername() != null) {
            AccountEnterprise accountEnterprise = enterpriseRepository.findByUsername(CouponDto.getEnterpriseUsername());
            if (accountEnterprise != null) {
                coupon.setAccountEnterprise(accountEnterprise);
            }
        }
        couponRepository.save(coupon);
        CouponRoom couponRoom = new CouponRoom();
        couponRoom.setCoupon(coupon);
        couponRoom.setRoom(room);
        couponRoomRepository.save(couponRoom);
        return couponMapper.toResponse(coupon);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Integer id, CouponDto couponDto) throws Exception {
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new Exception("Coupon is not found"));
        existingCoupon = couponMapper.toEntity(couponDto);
        List<String> allowedDiscountTypes = Arrays.asList("PERCENT", "FIXED_AMOUNT");
        if (!allowedDiscountTypes.contains(couponDto.getDiscountType())) {
            throw new Exception("The loai giam gia khong hop le");
        }
        if (existingCoupon.getStartDate().isBefore(LocalDate.now())) {
            throw new Exception("Shopping date must be at least today!");
        }
        if (existingCoupon.getDiscountType().equals(DiscountType.PERCENT)) {
            BigDecimal discountValue = existingCoupon.getDiscountValue();
            int percent = discountValue.intValue();
            if (percent < 0 || percent > 100) {
                throw new AppException(ErrorType.percentIsUnprocessed);
            }
        }
        if (couponDto.getEnterpriseUsername() != null) {
            AccountEnterprise accountEnterprise = enterpriseRepository.findByUsername(couponDto.getEnterpriseUsername());
            if (accountEnterprise != null) {
                existingCoupon.setAccountEnterprise(accountEnterprise);
            }
        }
        existingCoupon.setId(id);
        couponRepository.saveAndFlush(existingCoupon);
        return couponMapper.toResponse(existingCoupon);
    }

    @Override
    public Page<CouponResponse> getCoupons(PageRequest request, Long id) {
// <<<<<<< ductien
//         return couponRepository.getCouponByEnterpriseId(request, id).map(couponMapper::toResponse);
// =======
        if(id == null){
           return couponRepository.getCouponForAdmin(request).map(couponMapper::toResponse);
        }
        return couponRepository.getCouponByEnterpriseId(request,id).map(couponMapper::toResponse);
    }

    @Override
    public CouponResponse getByCouponId(Integer id) throws Exception {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if (!coupon.isPresent()) {
            throw new Exception("Coupon is not exist");
        }
        return couponMapper.toResponse(coupon.get());
    }

    @Override
    @Transactional
    public void deleteCouponById(Integer id) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if (!coupon.isPresent()) {
            throw new AppException(ErrorType.notFound, "Coupon not found");
        }
        if (coupon.get().getUseCount() > 0) {
            coupon.get().setActive(false);
            couponRepository.save(coupon.get());
        } else  {
            couponRepository.delete(coupon.get());
        }
    }

    @Override
    public CouponResponse getByCouponCode(String code, String status) {
        return couponMapper.toResponse(couponRepository.findByCodeAndStatus(code, Boolean.valueOf(status)));
    }

    @Override
    public Page<Map<String, Object>> getByEnterpriseId(Pageable pageable, String status) {
        Long id = tokenMapperImpl.getIdEnterpriseByToken();
        Boolean isActive;
        if (status == null) {
            isActive = null;
        } else {
            isActive = Boolean.valueOf(status);
        }


        if (id != null) {
            Page<CouponResponse> couponResponses = couponRepository
                    .getCouponByEnterpriseIdAndStatus(pageable, id, Boolean.valueOf(status))
                    .map(couponMapper::toResponse);

            Page<Map<String, Object>> response = couponResponses.map(coupon -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", coupon.getId());
                map.put("code", coupon.getCode());
                map.put("discountType", coupon.getDiscountType());
                map.put("discountValue", coupon.getDiscountValue());
                map.put("useLimit", coupon.getUseLimit());
                map.put("usedCount", coupon.getUseCount());
                map.put("startDate", coupon.getStartDate());
                map.put("endDate", coupon.getEndDate());
                map.put("isActive", coupon.getIsActive());

                List<String> roomCodes = couponRoomRepository
                        .findRoomCodesByCouponId(coupon.getId().longValue());
                if (!roomCodes.isEmpty()) {
                    map.put("roomCode", roomCodes);
                }

                return map;
            });

            return response;
        }
        throw new AppException(ErrorType.notFound, "Enterprise not found");
    }

    @Override
    public Page<Map<String, Object>> searchEnterprise(String keyword, String status, String discountType, Pageable pageable) {
        Long id = tokenMapperImpl.getIdEnterpriseByToken();
        Boolean isActive;
        if (status == null) {
            isActive = null;
        } else {
            isActive = Boolean.valueOf(status);
        }

        DiscountType type;
        if (discountType == null || discountType.isBlank()) {
            type = null;
        } else {
            try {
                type = DiscountType.valueOf(discountType.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                throw new AppException(ErrorType.inputFieldInvalid, "Invalid discount type: " + discountType);
            }
        }


        if (id != null) {
            Page<CouponResponse> couponResponses = couponRepository
                    .searchEnterprise(keyword, isActive, type, id, pageable)
                    .map(couponMapper::toResponse);

            Page<Map<String, Object>> response = couponResponses.map(coupon -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", coupon.getId());
                map.put("code", coupon.getCode());
                map.put("discountType", coupon.getDiscountType());
                map.put("discountValue", coupon.getDiscountValue());
                map.put("useLimit", coupon.getUseLimit());
                map.put("usedCount", coupon.getUseCount());
                map.put("startDate", coupon.getStartDate());
                map.put("endDate", coupon.getEndDate());
                map.put("isActive", coupon.getIsActive());

                List<String> roomCodes = couponRoomRepository
                        .findRoomCodesByCouponId(coupon.getId().longValue());
                if (!roomCodes.isEmpty()) {
                    map.put("roomCode", roomCodes);
                }

                return map;
            });

            return response;
        }
        throw new AppException(ErrorType.notFound, "Enterprise not found");
    }


}
