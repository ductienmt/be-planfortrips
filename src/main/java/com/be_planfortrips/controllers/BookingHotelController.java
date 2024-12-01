package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingCustomer;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.services.interfaces.IBookingHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${api.prefix}/booking-hotels")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingHotelController {

    IBookingHotelService bookingHotelService;

    @GetMapping("all")
    public ResponseEntity<Set<BookingHotelResponse>> getAllBookingHotel() {
        Set<BookingHotelResponse> responses = bookingHotelService.getAllBookingHotel();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("getById/{bookingId}")
    public ResponseEntity<BookingHotelResponse> getBookingHotelByBookingId(
            @PathVariable Long bookingId) {
        BookingHotelResponse response = bookingHotelService.getBookingHotelByBookingId(bookingId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingHotelResponse>> getBookingHotelsByUserId(
            @PathVariable Long userId) {
        List<BookingHotelResponse> responses = bookingHotelService.getBookingHotelByUserId(userId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBookingHotel(
            @RequestBody BookingHotelDto bookingHotelDto) {
        BookingHotelResponse response = bookingHotelService.createBookingHotel(bookingHotelDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("update/{bookingId}")
    public ResponseEntity<BookingHotelResponse> updateBookingHotel(
            @PathVariable Long bookingId, @RequestBody BookingHotelDto bookingHotelDto) {
        BookingHotelResponse response = bookingHotelService.updateBookingHotel(bookingHotelDto, bookingId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("delete/{bookingId}")
    public ResponseEntity<Void> deleteBookingHotelByBookingId(
            @PathVariable Long bookingId) {
        bookingHotelService.deleteBookingHotelByBookingId(bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("getUsers")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String status) {
        List<BookingCustomer> customers = bookingHotelService.findCustomersByEnterpriseId(status);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }


}
