package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.services.interfaces.IBookingHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("${api.prefix}/bookings-hotel")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BookingHotelController {

    IBookingHotelService bookingHotelService;

    @GetMapping
    public ResponseEntity<
            Set<BookingHotelResponse>>
             getAllBookingHotel() {
        Set<BookingHotelResponse> bookingHotelResponses = bookingHotelService.getAllBookingHotel();
        return ResponseEntity.ok(bookingHotelResponses);
    }

    @GetMapping("/{bookingHotelId}")
    public ResponseEntity<BookingHotelResponse> getBookingHotelId(
            @PathVariable Long bookingHotelId
    ) {
        BookingHotelResponse bookingHotelResponse = bookingHotelService.getBookingHotelById(bookingHotelId);
        return ResponseEntity.ok(bookingHotelResponse);
    }

    @PostMapping
    public ResponseEntity<BookingHotelResponse> createBookingHotel(
            @RequestBody BookingHotelDto bookingHotelDto
            ) {
        BookingHotelResponse bookingHotelResponse = bookingHotelService.createBookingHotel(bookingHotelDto);
        return ResponseEntity.ok(bookingHotelResponse);
    }

    @PutMapping("/{bookingHotelId}")
    public ResponseEntity<BookingHotelResponse> updateBookingHotel(
            @PathVariable Long bookingHotelId, @RequestBody BookingHotelDto bookingHotelDto
    ) {
        BookingHotelResponse bookingHotelResponse = bookingHotelService.updateBookingHotel(bookingHotelId, bookingHotelDto);
        return ResponseEntity.ok(bookingHotelResponse);
    }


    @DeleteMapping("/{bookingHotelId}")
    public ResponseEntity<Void> deleteBookingHotelId(
            @PathVariable Long bookingHotelId
    ) {
       bookingHotelService.deleteBookingHotelById(bookingHotelId);
        return ResponseEntity.noContent().build();
    }
}
