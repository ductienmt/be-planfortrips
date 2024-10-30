package com.be_planfortrips.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleResponse {

    Integer Id;
    String routeId;
    String code;
    String carCompanyName;
    BigDecimal priceForOneTicket;
    BigDecimal carCompanyRating;
    Long countSeatsEmpty;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
}
