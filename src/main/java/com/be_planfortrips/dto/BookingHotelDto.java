package com.be_planfortrips.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingHotelDto {
    List<BookingHotelDetailDto> bookingHotelDetailDto;
//    Long userId;
    Long paymentId;
    BigDecimal totalPrice;;

}
