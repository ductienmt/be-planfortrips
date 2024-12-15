package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.CouponDto;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.CouponResponse;
import com.be_planfortrips.mappers.impl.PageMapperImpl;
import com.be_planfortrips.services.interfaces.ICouponService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponController {
    private static final Logger log = LoggerFactory.getLogger(CouponController.class);
    ICouponService iCouponService;
    private final PageMapperImpl pageMapperImpl;

    @PostMapping("/create")
    public ResponseEntity<?> createCoupon(@RequestBody @Valid CouponDto CouponDTO,
                                          BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            CouponResponse CouponResponse = iCouponService.createCoupon(CouponDTO);
            return ResponseEntity.ok(CouponResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create-coupon-room")
    public ResponseEntity<?> createCouponRoom(@RequestBody @Valid CouponDto CouponDTO, @RequestParam Long roomId,
                                              BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            CouponResponse CouponResponse = iCouponService.createCouponRoom(CouponDTO, roomId);
            return ResponseEntity.ok(CouponResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable Integer id, @RequestBody @Valid CouponDto CouponDTO,
                                          BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            CouponResponse CouponResponse = iCouponService.updateCoupon(id, CouponDTO);
            return ResponseEntity.ok(CouponResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getCoupons(@RequestParam int page,
                                        @RequestParam int limit,
                                        @RequestParam(defaultValue = "") Long id) {
        try {
            PageRequest request = PageRequest.of(page, 1000000000, Sort.by("endDate").ascending());
            int totalPage = 0;
            Page<CouponResponse> CouponResponses = iCouponService.getCoupons(request, id);
            totalPage = CouponResponses.getTotalPages();
            TListResponse<CouponResponse> listResponse = new TListResponse<>();
            listResponse.setListResponse(CouponResponses.toList());
            listResponse.setTotalPage(totalPage);
            return ResponseEntity.ok(listResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<?> deleteCouponById(@PathVariable Integer id) throws Exception {
        iCouponService.deleteCouponById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable Integer id) {
        try {
            CouponResponse response = iCouponService.getByCouponId(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("getByCode")
    public ResponseEntity<?> getCouponByCode(@RequestParam String code, @RequestParam(defaultValue = "true", required = false) String status) {
        try {
            CouponResponse response = iCouponService.getByCouponCode(code, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("getByEnterpriseId")
    public ResponseEntity<?> getCouponByEnterpriseId(@RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                                     @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                     @RequestParam(defaultValue = "", required = false) String status,
                                                     @RequestParam(defaultValue = "id", required = false) String sortBy,
                                                     @RequestParam(defaultValue = "ASC", required = false) String sortType) {
        try {
            var pageable = pageMapperImpl.customPage(pageNo, pageSize, sortBy, sortType);
            Page<Map<String, Object>> response = iCouponService.getByEnterpriseId(pageable, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("searchEnterprise")
    public ResponseEntity<?> searchEnterprise(
            @RequestParam(defaultValue = "",required = false) String keyword,
            @RequestParam(defaultValue = "", required = false) String status,
            @RequestParam(defaultValue = "",required = false) String discountType,
            @RequestParam(defaultValue = "0", required = false) Integer pageNo,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "ASC", required = false) String sortType) {
        try {
            var pageable = pageMapperImpl.customPage(pageNo, pageSize, sortBy, sortType);
            Page<Map<String, Object>> response = iCouponService.searchEnterprise(keyword, status, discountType, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
