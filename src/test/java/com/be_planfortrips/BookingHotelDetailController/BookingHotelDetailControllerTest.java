package com.be_planfortrips.BookingHotelDetailController;

import com.be_planfortrips.controllers.BookingHotelDetailController;
import com.be_planfortrips.dto.response.BookingHotelDetailResponse;
import com.be_planfortrips.services.interfaces.IBookingHotelDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingHotelDetailControllerTest {

    @InjectMocks
    private BookingHotelDetailController bookingHotelDetailController;

    @Mock
    private IBookingHotelDetailService bookingHotelDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBookingHotelDetail() {
        List<BookingHotelDetailResponse> mockResponse = Arrays.asList(new BookingHotelDetailResponse());
        when(bookingHotelDetailService.getAllBookingHotelDetail()).thenReturn(mockResponse);

        ResponseEntity<List<BookingHotelDetailResponse>> response = bookingHotelDetailController.getAllBookingHotelDetail();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void getBookingHotelDetailById() {
        // Giả lập dữ liệu trả về từ service
        UUID bookingHotelDtlId = UUID.randomUUID();
        BookingHotelDetailResponse mockResponse = new BookingHotelDetailResponse();
        when(bookingHotelDetailService.getBookingHotelById(bookingHotelDtlId)).thenReturn(mockResponse);

        ResponseEntity<BookingHotelDetailResponse> response = bookingHotelDetailController.getBookingHotelDetailById(bookingHotelDtlId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void getBookingHotelDetailById1() {
        UUID bookingHotelDtlId = UUID.randomUUID();
        when(bookingHotelDetailService.getBookingHotelById(bookingHotelDtlId))
                .thenThrow(new RuntimeException(" ID"));

        try {
            bookingHotelDetailController.getBookingHotelDetailById(bookingHotelDtlId);
        } catch (RuntimeException ex) {
            assertEquals("ID", ex.getMessage());
        }
    }
}
