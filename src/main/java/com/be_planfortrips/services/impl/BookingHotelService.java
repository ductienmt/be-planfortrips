package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.BookingHotelRepository;
import com.be_planfortrips.services.interfaces.IBookingHotelService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class BookingHotelService implements IBookingHotelService {

    BookingHotelRepository bookingHotelRepository;
    MapperInterface<BookingHotelResponse, BookingHotel, BookingHotelDto> bookingMapper;

    @Override
    public Set<BookingHotelResponse> getAllBookingHotel() {
        return new HashSet<>(
                bookingHotelRepository.findAll().stream().map(bookingMapper::toResponse).toList()
        );
    }

    @Override
    public BookingHotelResponse getBookingHotelById(Long bookingHotelId) {
        BookingHotel bookingHotel = bookingHotelRepository.findById(bookingHotelId).orElseThrow();
        return bookingMapper.toResponse(bookingHotel);
    }

    @Override
    public BookingHotelResponse createBookingHotel(BookingHotelDto bookingHotelDto) {
        BookingHotel bookingHotel = bookingMapper.toEntity(bookingHotelDto);
        return bookingMapper.toResponse(bookingHotelRepository.save(bookingHotel));
    }

    @Override
    public void deleteBookingHotelById(Long bookingHotelId) {
        BookingHotel bookingHotel = bookingHotelRepository.findById(bookingHotelId).orElseThrow();
        bookingHotelRepository.delete(bookingHotel);
    }

    @Override
    public BookingHotelResponse updateBookingHotel(Long bookingHotelId, BookingHotelDto bookingHotelDto) {
        BookingHotel bookingHotelOld = bookingHotelRepository.findById(bookingHotelId).orElseThrow();
        BookingHotel bookingHotelNew = bookingMapper.toEntity(bookingHotelDto);
        bookingHotelNew.setId(bookingHotelId);
        return bookingMapper.toResponse(bookingHotelRepository.save(bookingHotelNew));
    }
}
