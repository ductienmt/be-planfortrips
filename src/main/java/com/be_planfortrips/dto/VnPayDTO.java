package com.be_planfortrips.dto;

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
public class VnPayDTO {
    @JsonProperty("ticket_id")
    List<Integer> ticketId;
    @JsonProperty("booking_id")
    List<Long> bookingId;
    BigDecimal amount;
    @JsonProperty("bank_code")
    String bankCode = "NCB";
}
