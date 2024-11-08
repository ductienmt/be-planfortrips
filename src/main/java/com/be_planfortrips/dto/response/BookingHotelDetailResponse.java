package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.BookingHotelDetail;
import com.be_planfortrips.entity.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BookingHotelDetailResponse {

    UUID bookingHotelDetailId;
    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;
    Status status;
    BigDecimal price;
    Long userId;
    Long roomId;


}
