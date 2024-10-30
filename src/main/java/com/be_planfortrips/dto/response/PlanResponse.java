package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.StatusPlan;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanResponse {
    Long plan_id;
    String plan_name;
    LocalDate start_date;
    LocalDate end_date;
    BigDecimal budget;
    BigDecimal total_price;
    StatusPlan status;
    String destination;
    String origin_location;
    Integer numberPeople;
}
