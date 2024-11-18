package com.be_planfortrips.controllers;

import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.Status;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.BookingHotelRepository;
import com.be_planfortrips.repositories.HotelRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/vietqr")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class VietQrController {


    BookingHotelRepository bookingHotelRepository;

    @PostMapping("/{bookingHotelId}")
    public ResponseEntity<?> paymentVietQr(
          @PathVariable("bookingHotelId") Long bookingHotelId
    ) {
        BookingHotel bookingHotel =
                bookingHotelRepository.findById(bookingHotelId).orElseThrow(
                        () -> new AppException(ErrorType.notFound)
                );
        bookingHotel.setStatus(Status.Complete);
        bookingHotel.getBookingHotelDetails().forEach((bk) -> {
            bk.setStatus(Status.Complete);
        });
        bookingHotelRepository.save(bookingHotel);
        return ResponseEntity.ok("OKKKKKK");
    }
}
