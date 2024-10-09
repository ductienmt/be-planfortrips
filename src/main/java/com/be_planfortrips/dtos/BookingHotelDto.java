     package com.be_planfortrips.dto;

     import com.be_planfortrips.entity.Status;
     import com.fasterxml.jackson.annotation.JsonFormat;
     import lombok.AccessLevel;
     import lombok.AllArgsConstructor;
     import lombok.Data;
     import lombok.NoArgsConstructor;
     import lombok.experimental.FieldDefaults;

     import java.time.LocalDateTime;

     @Data
     @FieldDefaults(level = AccessLevel.PRIVATE)
     @AllArgsConstructor
     @NoArgsConstructor
     public class BookingHotelDto {
          Long userId;
          Long roomId;

          @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
          LocalDateTime checkInTime;

          @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
          LocalDateTime checkOutTime;
          Double totalPrice;
          Status status;

          @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
          LocalDateTime createAt;

          @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
          LocalDateTime updateAt;
          Long paymentId;
     }
