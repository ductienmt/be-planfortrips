package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MomoDTO {
    @JsonProperty("order_id")
    int orderId;
    long amount;
    @JsonProperty("bank_code")
    String bankCode = "NCB";
}
