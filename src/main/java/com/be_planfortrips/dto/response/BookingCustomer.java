package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Status;
import com.be_planfortrips.entity.TypeOfRoom;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCustomer {
    private Long bookingId;
    private String customerName;
    private String customerPhoneNumber;
    private String roomName;
    private BigDecimal totalPrice;
    private String roomType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private Timestamp checkInDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private Timestamp checkOutDate;
    private String bookingStatus;
}
