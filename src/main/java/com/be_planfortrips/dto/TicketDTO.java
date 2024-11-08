package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketDTO {
    @JsonProperty("schedule_id")
    @Min(value = 1,message = "schedule_id must be greater than 1 star")
    Integer scheduleId;
    @JsonProperty("user_name")
    @NotBlank(message = "userName is required")
    String userName;
    @JsonProperty("total_price")
    @Min(value = 1,message = "Price must be greater than 1")
    BigDecimal totalPrice;
    @JsonProperty("payment_id")
    Long paymentId;
    @JsonProperty("status")
    @NotBlank(message = "status is required")
    String status;
    List<Integer> seatIds;
    String codeCoupon;
}
