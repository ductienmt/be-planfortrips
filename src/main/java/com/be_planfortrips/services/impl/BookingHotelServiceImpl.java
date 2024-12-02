package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingCustomer;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.Status;
import com.be_planfortrips.entity.TypeOfRoom;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.BookingHotelMapper;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.BookingHotelRepository;
import com.be_planfortrips.repositories.UserRepository;
import com.be_planfortrips.services.interfaces.IBookingHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BookingHotelServiceImpl implements IBookingHotelService {

    BookingHotelRepository bookingHotelRepository;
    UserRepository userRepository;
    BookingHotelMapper bookingHotelMapper;
    private final TokenMapperImpl tokenMapperImpl;


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
        bookingHotel.setUser(userRepository.findById(tokenMapperImpl.getIdUserByToken()).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        ));
        bookingHotel.setStatus(Status.Pending);
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

    @Override
    public List<BookingCustomer> findCustomersByEnterpriseId(String status) {
        Long enterpriseId = tokenMapperImpl.getIdEnterpriseByToken();


        List<Object[]> objects = new ArrayList<>();

        if ("not_used".equals(status)) {
            objects = bookingHotelRepository.findCustomersByEnterpriseIdNotUse(enterpriseId);
        } else if ("in_use".equals(status)) {
            objects = bookingHotelRepository.findCustomersByEnterpriseIdInUse(enterpriseId);
        } else if ("used".equals(status)) {
            objects = bookingHotelRepository.findCustomersByEnterpriseused(enterpriseId);
        } else {
            objects = bookingHotelRepository.findAllCustomersByEnterpriseId(enterpriseId);
        }

        List<BookingCustomer> bookingCustomers = new ArrayList<>();

        for (Object[] obj : objects) {
            BookingCustomer bookingCustomer = new BookingCustomer();

            bookingCustomer.setBookingId((Long) obj[0]);
            bookingCustomer.setCustomerName((String) obj[1]);
            bookingCustomer.setCustomerPhoneNumber((String) obj[2]);
            bookingCustomer.setRoomName((String) obj[3]);
            bookingCustomer.setTotalPrice((BigDecimal) obj[4]);
            bookingCustomer.setRoomType((String) obj[5]);
            bookingCustomer.setCheckInDate((Timestamp) obj[6]);
            bookingCustomer.setCheckOutDate((Timestamp) obj[7]);
            bookingCustomer.setBookingStatus((String) obj[8]);

            bookingCustomers.add(bookingCustomer);
        }

        return bookingCustomers;
    }


}
