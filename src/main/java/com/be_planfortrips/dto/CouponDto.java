package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponDto {
    @JsonProperty("code")
    @NotBlank(message = "code is required")
    String code;
    @JsonProperty("discount_type")
    @NotBlank(message = "discount_type is required")
    String discountType;
    @JsonProperty("discount_value")
    Double discountValue;
    @JsonProperty("start_date")
    LocalDate startDate;
    @JsonProperty("end_date")
    LocalDate endDate;
    @JsonProperty("use_limit")
    @Min(value = 1, message = "Use limit must be greater than 1")
    Integer useLimit;
    @JsonProperty("use_count")

//    @Min(value = 1, message = "Use count must be greater than 1")
    Integer useCount;

    @JsonProperty("is_active")
    Boolean isActive;
    @JsonProperty("enterprise_username")
    String enterpriseUsername;
}
