package com.be_planfortrips.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingHotelDto {
    private Integer id;
    private Integer userId;
    private Integer roomId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Double totalPrice;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Integer paymentId;
}
