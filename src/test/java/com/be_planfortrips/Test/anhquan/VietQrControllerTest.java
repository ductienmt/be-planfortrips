package com.be_planfortrips.Test.anhquan;

import com.be_planfortrips.controllers.VietQrController;
import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.Plan;
import com.be_planfortrips.entity.Ticket;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.repositories.BookingHotelRepository;
import com.be_planfortrips.repositories.PlanRepository;
import com.be_planfortrips.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VietQrControllerTest {

    @Mock
    private BookingHotelRepository bookingHotelRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private VietQrController vietQrController;

    private BookingHotel bookingHotel;
    private Plan plan;
    private Ticket departureTicket;
    private Ticket returnTicket;

    @BeforeEach
    void setUp() {
        // Khởi tạo đối tượng giả để sử dụng trong các bài test
        bookingHotel = new BookingHotel();
        plan = new Plan();
        departureTicket = new Ticket();
        returnTicket = new Ticket();
    }

    @Test
    void testPaymentVietQr_Success() {
        // Giả lập hành vi của repository
        when(bookingHotelRepository.findById(1L)).thenReturn(java.util.Optional.of(bookingHotel));

        // Gọi controller
        ResponseEntity<?> response = vietQrController.paymentVietQr(1L);

        // Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OKKKKKK", response.getBody());

        // Kiểm tra hành vi save của repository
        verify(bookingHotelRepository, times(1)).save(bookingHotel);
    }

    @Test
    void testPaymentVietQr_Failure() {
        // Giả lập khi không tìm thấy booking hotel
        when(bookingHotelRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Gọi controller
        Exception exception = assertThrows(AppException.class, () -> vietQrController.paymentVietQr(1L));

        // Kiểm tra ngoại lệ
        assertFalse(exception.getMessage().contains("Not Found"));
    }

    @Test
    void testPaymentTransportation_Success() {
        // Giả lập hành vi của repository
        when(ticketRepository.findById(1)).thenReturn(java.util.Optional.of(departureTicket));

        // Gọi controller
        ResponseEntity<?> response = vietQrController.paymentTransportation(1);

        // Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Đặt vé thành công", response.getBody());

        // Kiểm tra hành vi save của repository
        verify(ticketRepository, times(1)).save(departureTicket);
    }

    @Test
    void testPaymentTransportation_Failure() {
        // Giả lập khi không tìm thấy ticket
        when(ticketRepository.findById(1)).thenReturn(java.util.Optional.empty());

        // Gọi controller
        Exception exception = assertThrows(AppException.class, () -> vietQrController.paymentTransportation(1));

        // Kiểm tra ngoại lệ
        assertFalse(exception.getMessage().contains("Không tìm thấy vé"));
    }

    @Test
    void testPaymentPlan_Success() {
        // Giả lập hành vi của repository
        when(planRepository.findById(1L)).thenReturn(java.util.Optional.of(plan));
        when(bookingHotelRepository.findById(1L)).thenReturn(java.util.Optional.of(bookingHotel));
        when(ticketRepository.findById(1)).thenReturn(java.util.Optional.of(departureTicket));
        when(ticketRepository.findById(2)).thenReturn(java.util.Optional.of(returnTicket));

        // Gọi controller
        ResponseEntity<?> response = vietQrController.paymentPlan(1L, 1L, 1, 2);

        // Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Đặt vé thành công", response.getBody());

        // Kiểm tra hành vi save của repository
        verify(planRepository, times(1)).save(plan);
        verify(bookingHotelRepository, times(1)).save(bookingHotel);
        verify(ticketRepository, times(2)).save(departureTicket);
        verify(ticketRepository, times(2)).save(returnTicket);
    }

    @Test
    void testPaymentPlan_Failure() {
        // Giả lập khi không tìm thấy kế hoạch
        when(planRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Gọi controller
        Exception exception = assertThrows(AppException.class, () -> vietQrController.paymentPlan(1L, 1L, 1, 2));

        // Kiểm tra ngoại lệ
        assertFalse(exception.getMessage().contains("Không tìm thấy plan"));
    }
}
