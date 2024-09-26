package com.be_planfortrips.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingPlaneDto {
    private Integer bookingPlaneId;
    private Integer ticketId;
    private Integer userId;
    private LocalDateTime bookDate;
    private String status;
    private Double totalPrice;
    private Integer serviceId;
    private Integer paymentId;
}
