package com.be_planfortrips.dto;

import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingHotelDetailDto {

    Long roomId;
    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;
    BigDecimal price;
}
