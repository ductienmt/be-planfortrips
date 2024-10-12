package com.be_planfortrips.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingHotelDto {

    Set<BookingHotelDetailDto> bookingHotelDetailDto;
    Long userId;
    Long paymentId;

}
