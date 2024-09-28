package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
     String paymentMethod;
     Double amount;
     String status;
     LocalDateTime createAt;
}
