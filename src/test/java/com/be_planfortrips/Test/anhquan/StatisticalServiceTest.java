//package com.be_planfortrips.Test.anhquan;
//
//import com.be_planfortrips.dto.sql.StatisticalBookingHotelDetail;
//import com.be_planfortrips.repositories.BookingHotelDetailRepository;
//import com.be_planfortrips.services.StatisticalService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class StatisticalServiceTest {
//
//    @Mock
//    private BookingHotelDetailRepository bookingHotelDetailRepository;
//
//    @InjectMocks
//    private StatisticalService statisticalService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testStatisticalBookingHotelByYear() {
//        Integer year = 2024;
//
//        // Mock StatisticalBookingHotelDetail
//        StatisticalBookingHotelDetail detail1 = mock(StatisticalBookingHotelDetail.class);
//        StatisticalBookingHotelDetail detail2 = mock(StatisticalBookingHotelDetail.class);
//
//        // Cấu hình hành vi cho các phương thức trừu tượng
//        when(detail1.getMonth()).thenReturn("2024-01");
//        when(detail1.getTotalDetails()).thenReturn(10);
//
//        when(detail2.getMonth()).thenReturn("2024-02");
//        when(detail2.getTotalDetails()).thenReturn(15);
//
//        List<StatisticalBookingHotelDetail> statisticalBookingHotelDetails = Arrays.asList(detail1, detail2);
//
//        // Mock repository
//        when(bookingHotelDetailRepository.StatisticalBookingHotelByYear(year)).thenReturn(statisticalBookingHotelDetails);
//
//        // Gọi service
//        List<StatisticalBookingHotelDetail> result = statisticalService.statisticalBookingHotelByYear(year);
//
//        // Kiểm tra kết quả
//        assertNotNull(result);
//        assertEquals(2, result.size());
//}
//}
