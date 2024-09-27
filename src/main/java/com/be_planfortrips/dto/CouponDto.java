package com.be_planfortrips.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponDto {
    private Integer id;
    private String code;
    private String discountType;
    private Double discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer useLimit;
    private Integer useCount;
    private Boolean isActive;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
