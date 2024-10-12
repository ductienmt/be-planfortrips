package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.BookingHotelMapper;
import com.be_planfortrips.repositories.BookingHotelRepository;
import com.be_planfortrips.repositories.UserRepository;
import com.be_planfortrips.services.interfaces.IBookingHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BookingHotelServiceImpl implements IBookingHotelService {

    BookingHotelRepository bookingHotelRepository;
    UserRepository userRepository;
    BookingHotelMapper bookingHotelMapper;


    @Override
    public Set<BookingHotelResponse> getAllBookingHotel() {
        List<BookingHotel> bookingHotel = bookingHotelRepository.findAll();
        return new HashSet<>(
                bookingHotel.stream().map(bookingHotelMapper::toResponse).toList()
        );
    }

    @Override
    public BookingHotelResponse getBookingHotelByBookingId(Long bookingId) {
        BookingHotel hotel = bookingHotelRepository.findById(bookingId).orElseThrow();
        return bookingHotelMapper.toResponse(hotel);
    }

    @Override
    public List<BookingHotelResponse> getBookingHotelByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );
        List<BookingHotel> bookingHotels = bookingHotelRepository.getBookingHotelByUser(user);
        return bookingHotels.stream().map(bookingHotelMapper::toResponse).toList();
    }

    @Override
    public BookingHotelResponse createBookingHotel(BookingHotelDto bookingHotelDto) {
        BookingHotel bookingHotel = bookingHotelMapper.toEntity(bookingHotelDto);
        return bookingHotelMapper.toResponse(bookingHotelRepository.save(bookingHotel));
    }

    @Override
    public BookingHotelResponse updateBookingHotel(BookingHotelDto bookingHotelDto, Long bookingHotelId) {
        BookingHotel bookingHotel = bookingHotelRepository.findById(bookingHotelId).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );
        return bookingHotelMapper.toResponse(bookingHotel);
    }

    @Override
    public void deleteBookingHotelByBookingId(Long bookingId) {
        bookingHotelRepository.findById(bookingId).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );
        bookingHotelRepository.deleteById(bookingId);
    }
}
