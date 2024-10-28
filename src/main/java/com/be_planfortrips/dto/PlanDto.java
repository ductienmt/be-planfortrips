package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanDto {
    String planName;
    Date startDate;
    Date endDate;
    String location;
    String destination;
    BigDecimal budget;
    Integer numberPeople;
    BigDecimal totalPrice;
    List<PlanDetailDto> planDetails;
}
