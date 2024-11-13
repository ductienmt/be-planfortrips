package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Coupon;
import com.be_planfortrips.entity.Payment;
import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.entity.Seat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
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
public class TicketResponse {
    @JsonProperty("ticket_id")
    Integer ticketId;
    Schedule schedule;
    @JsonProperty("user_id")
    Long user_id;
    @JsonProperty("total_price")
    BigDecimal totalPrice;
    Payment payment;
    @JsonProperty("status")
    String status;
    List<Seat> seats;
    List<Coupon> coupons;
    @JsonProperty("create_at")
    LocalDateTime createAt;
    @JsonProperty("update_at")
    LocalDateTime updateAt;
}
