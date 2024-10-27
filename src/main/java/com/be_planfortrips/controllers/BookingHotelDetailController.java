package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.response.BookingHotelDetailResponse;
import com.be_planfortrips.services.interfaces.IBookingHotelDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/booking-hotel-details")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingHotelDetailController {

    IBookingHotelDetailService bookingHotelDetailService;

    @GetMapping("/all")
    public ResponseEntity<List<BookingHotelDetailResponse>> getAllBookingHotelDetail() {
        List<BookingHotelDetailResponse> responses = bookingHotelDetailService.getAllBookingHotelDetail();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("getById/{bookingHotelDtlId}")
    public ResponseEntity<BookingHotelDetailResponse> getBookingHotelDetailById(
            @PathVariable UUID bookingHotelDtlId
            ) {
        BookingHotelDetailResponse response = bookingHotelDetailService.getBookingHotelById(bookingHotelDtlId);
        return ResponseEntity.ok(response);
    }
}
