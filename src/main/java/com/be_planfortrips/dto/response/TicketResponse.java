package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Payment;
import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.entity.Seat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResponse {
    Integer ticketId;
    Schedule schedule;
    String userName;
    BigDecimal totalPrice;
    Payment payment;
    @JsonProperty("status")
    String status;
    List<Seat> seats;
}
