package com.be_planfortrips.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataEssentialPlan {
    String location;
    String destination;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer numberPeople;
    BigDecimal budget;
}
