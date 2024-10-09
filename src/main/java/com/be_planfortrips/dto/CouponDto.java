package com.be_planfortrips.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponDto {
    String code;
    String discountType;
    Double discountValue;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer useLimit;
    Integer useCount;
    Boolean isActive;
    LocalDateTime createAt;
    LocalDateTime updateAt;
}
