package com.be_planfortrips.BookingHotelController;

import com.be_planfortrips.controllers.BookingHotelController;
import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.services.interfaces.IBookingHotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingHotelControllerTest {

    @InjectMocks
    private BookingHotelController bookingHotelController;

    @Mock
    private IBookingHotelService bookingHotelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getAllBookingHotel() {
        Set<BookingHotelResponse> mockResponse = Set.of(new BookingHotelResponse());
        when(bookingHotelService.getAllBookingHotel()).thenReturn(mockResponse);

        ResponseEntity<Set<BookingHotelResponse>> response = bookingHotelController.getAllBookingHotel();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void getAllBookingHotel1() {
        when(bookingHotelService.getAllBookingHotel()).thenReturn(Set.of());

        ResponseEntity<Set<BookingHotelResponse>> response = bookingHotelController.getAllBookingHotel();

        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    void getBookingHotelByBookingId() {
        Long bookingId = 1L;
        BookingHotelResponse mockResponse = new BookingHotelResponse();
        when(bookingHotelService.getBookingHotelByBookingId(bookingId)).thenReturn(mockResponse);

        ResponseEntity<BookingHotelResponse> response = bookingHotelController.getBookingHotelByBookingId(bookingId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }




    @Test
    void getBookingHotelsByUserId() {
        Long userId = 1L;
        List<BookingHotelResponse> mockResponse = Arrays.asList(new BookingHotelResponse());
        when(bookingHotelService.getBookingHotelByUserId(userId)).thenReturn(mockResponse);

        ResponseEntity<List<BookingHotelResponse>> response = bookingHotelController.getBookingHotelsByUserId(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void getBookingHotelsByUserId1() {
        Long userId = 1L;
        when(bookingHotelService.getBookingHotelByUserId(userId)).thenReturn(Arrays.asList());

        ResponseEntity<List<BookingHotelResponse>> response = bookingHotelController.getBookingHotelsByUserId(userId);

        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    void createBookingHotel() {
        BookingHotelDto bookingHotelDto = new BookingHotelDto();
        BookingHotelResponse mockResponse = new BookingHotelResponse();
        when(bookingHotelService.createBookingHotel(bookingHotelDto)).thenReturn(mockResponse);

        ResponseEntity<?> response = bookingHotelController.createBookingHotel(bookingHotelDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }



    // Test cho updateBookingHotel
    @Test
    void updateBookingHotel() {
        Long bookingId = 1L;
        BookingHotelDto bookingHotelDto = new BookingHotelDto();
        BookingHotelResponse mockResponse = new BookingHotelResponse();
        when(bookingHotelService.updateBookingHotel(bookingHotelDto, bookingId)).thenReturn(mockResponse);

        ResponseEntity<BookingHotelResponse> response = bookingHotelController.updateBookingHotel(bookingId, bookingHotelDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }




    @Test
    void deleteBookingHotelByBookingId() {
        Long bookingId = 1L;
        doNothing().when(bookingHotelService).deleteBookingHotelByBookingId(bookingId);

        ResponseEntity<Void> response = bookingHotelController.deleteBookingHotelByBookingId(bookingId);

        assertEquals(204, response.getStatusCodeValue());
        verify(bookingHotelService, times(1)).deleteBookingHotelByBookingId(bookingId);
    }


}
