package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingPlaneDto {
    Integer ticketId;
    Integer userId;
    LocalDateTime bookDate;
    String status;
    Double totalPrice;
    Integer serviceId;
    Integer paymentId;
}
