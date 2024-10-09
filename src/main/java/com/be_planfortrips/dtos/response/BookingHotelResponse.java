package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Payment;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.entity.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BookingHotelResponse {

    Long bookingHotelId;
    Room room;
    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    BigDecimal totalPrice;
    Status status;
    Payment payment;


}
