package com.be_planfortrips.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private Integer id;
    private String paymentMethod;
    private Double amount;
    private String status;
    private LocalDateTime createAt;
}
