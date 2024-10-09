package com.be_planfortrips.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataEssentialPlan {
    String location;
    String destination;
    String startDate;
    String endDate;
    Integer numberPeople;
    BigDecimal budget;
}
