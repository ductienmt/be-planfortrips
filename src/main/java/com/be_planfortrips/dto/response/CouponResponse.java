package com.be_planfortrips.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponResponse {
    @JsonProperty("coupon_id")
    Integer id;
    String code;
    String discountType;
    BigDecimal discountValue;
    LocalDate startDate;
    LocalDate endDate;
    int useLimit;
    int useCount;
    boolean isActive;
    Long enterpriseId;
}

