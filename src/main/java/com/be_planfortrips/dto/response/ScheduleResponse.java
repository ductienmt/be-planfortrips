package com.be_planfortrips.dto.response;


import com.be_planfortrips.entity.ScheduleSeat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    List<ScheduleSeat> scheduleSeat;
    String departureName;
    String arrivalName;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
}
