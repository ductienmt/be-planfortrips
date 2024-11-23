package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.StatusPlan;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate start_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate end_date;
    BigDecimal budget;
    BigDecimal total_price;
    BigDecimal discount_price;
    BigDecimal final_price;
    StatusPlan status;
    String destination;
    String origin_location;
    Integer numberPeople;
}
