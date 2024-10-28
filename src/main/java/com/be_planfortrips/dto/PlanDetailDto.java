package com.be_planfortrips.dto;

import com.be_planfortrips.entity.StatusPlan;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class PlanDetailDto {
    Long plan;
    String serviceId;
    Integer typeEdeId;
    BigDecimal totalPrice;
    Timestamp startDate;
    Timestamp endDate;
    Integer ticketId;
    StatusPlan status;
}
