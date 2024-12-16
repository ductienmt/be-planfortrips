package com.be_planfortrips.Test.anhquan;

import com.be_planfortrips.dto.HotelAmenitiesDto;
import com.be_planfortrips.dto.response.HotelAmenitiesResponse;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.HotelAmenities;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.mappers.impl.HotelAmenitiesMapper;
import com.be_planfortrips.repositories.HotelAmenitiesRepository;
import com.be_planfortrips.repositories.HotelRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.services.impl.HotelAmenitiesServiceImpl;
import com.be_planfortrips.services.impl.CloudinaryService;
import org.springframework.mock.web.MockMultipartFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelAmenitiesServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelAmenitiesRepository hotelAmenitiesRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private HotelAmenitiesMapper hotelAmenitiesMapper;

    @InjectMocks
    private HotelAmenitiesServiceImpl hotelAmenitiesService;

    private Hotel hotel;
    private HotelAmenitiesDto hotelAmenitiesDto;
    private HotelAmenities hotelAmenities;
    private HotelAmenitiesResponse hotelAmenitiesResponse;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);

        hotelAmenitiesDto = new HotelAmenitiesDto();
        hotelAmenitiesDto.setHotelId(1L);
        hotelAmenitiesDto.setName("Wi-Fi");
        hotelAmenitiesDto.setFee(new BigDecimal("100.0"));  // Sử dụng BigDecimal thay vì double


        hotelAmenities = new HotelAmenities();
        hotelAmenities.setId(1);
        hotelAmenities.setHotel(hotel);
        hotelAmenities.setName("Wi-Fi");
        hotelAmenities.setFee(new BigDecimal("100.0"));  // Sử dụng BigDecimal thay vì double

        hotelAmenitiesResponse = new HotelAmenitiesResponse();
        hotelAmenitiesResponse.setName("Wi-Fi");
        hotelAmenitiesResponse.setFee(new BigDecimal("100.0"));  // Sử dụng BigDecimal thay vì double


    }

    @Test
    void testCreate() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelAmenitiesMapper.toEntity(hotelAmenitiesDto)).thenReturn(hotelAmenities);
        when(hotelAmenitiesRepository.save(hotelAmenities)).thenReturn(hotelAmenities);
        when(hotelAmenitiesMapper.toResponse(hotelAmenities)).thenReturn(hotelAmenitiesResponse);

        HotelAmenitiesResponse result = hotelAmenitiesService.create(hotelAmenitiesDto);

        assertNotNull(result);
        assertEquals("Wi-Fi", result.getName());
        verify(hotelRepository, times(1)).findById(1L);
        verify(hotelAmenitiesRepository, times(1)).save(hotelAmenities);
    }

    @Test
    void testCreate_ThrowsExceptionWhenHotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> hotelAmenitiesService.create(hotelAmenitiesDto));
        assertEquals("HotelId không tồn tại", exception.getMessage());
    }

    @Test
    void testGetByHotelId() {
        // Giả lập khách sạn tồn tại
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        // Giả lập tiện ích của khách sạn
        HotelAmenities hotelAmenities = new HotelAmenities();
        hotelAmenities.setStatus(true); // Thiết lập trạng thái của tiện ích
        hotelAmenities.setName("Wi-Fi");
        hotelAmenities.setFee(new BigDecimal("100.0"));

        hotel.setHotelAmenities(List.of(hotelAmenities)); // Thiết lập danh sách tiện ích cho khách sạn

        // Giả lập mapper
        when(hotelAmenitiesMapper.toResponse(hotelAmenities)).thenReturn(hotelAmenitiesResponse);

        // Gọi phương thức cần test
        List<HotelAmenitiesResponse> result = hotelAmenitiesService.getByHotelId(1L, "true");

        // Kiểm tra kết quả
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Wi-Fi", result.get(0).getName());
    }

    @Test
    void testGetByHotelId_Fail() {
        // Giả lập khách sạn tồn tại
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        // Giả lập không có tiện ích nào cho khách sạn
        hotel.setHotelAmenities(List.of());  // Không có tiện ích nào trong danh sách

        // Gọi phương thức cần kiểm tra
        List<HotelAmenitiesResponse> result = hotelAmenitiesService.getByHotelId(1L, "true");

        // Kiểm tra kết quả
        assertNotNull(result);
        assertTrue(result.isEmpty());  // Kiểm tra rằng danh sách trả về là trống

        // Kiểm tra rằng phương thức findById của khách sạn đã được gọi đúng 1 lần
        verify(hotelRepository, times(1)).findById(1L);
    }


    @Test
    void testGetByHotelId_ThrowsExceptionWhenHotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> hotelAmenitiesService.getByHotelId(1L, "true"));
        assertEquals("HotelId không tồn tại", exception.getMessage()); // Kiểm tra thông điệp ngoại lệ
    }

    @Test
    void testDelete() {
        when(hotelAmenitiesRepository.findById(1)).thenReturn(Optional.of(hotelAmenities));

        hotelAmenitiesService.delete(1L);

        assertFalse(hotelAmenities.getStatus());
        verify(hotelAmenitiesRepository, times(1)).save(hotelAmenities);
    }


    @Test
    void testUploadIcon() throws Exception {
        MockMultipartFile icon = new MockMultipartFile("icon", "test-icon.jpg", "image/jpeg", new byte[0]);
        when(hotelAmenitiesRepository.findById(1)).thenReturn(Optional.of(hotelAmenities));
        when(cloudinaryService.uploadFile(any(), eq("hotel_amenities"))).thenReturn(Map.of("secure_url", "http://example.com/icon.jpg"));
        when(imageRepository.save(any())).thenReturn(new Image());

        hotelAmenitiesService.uploadIcon(1L, icon);

        assertNotNull(hotelAmenities.getIcon());
        verify(hotelAmenitiesRepository, times(1)).save(hotelAmenities);
    }



@Test
void testUploadIcon_ThrowsExceptionWhenHotelAmenitiesNotFound() throws Exception {
    // Đánh dấu stubbing là lenient để tránh lỗi UnnecessaryStubbingException
    lenient().when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

    // Tạo một MockMultipartFile giả lập
    MockMultipartFile someMockFile = new MockMultipartFile(
            "icon",             // Tên trường trong form
            "test-icon.jpg",    // Tên tệp
            "image/jpeg",       // Loại MIME
            new byte[0]         // Dữ liệu của tệp (trong trường hợp này là tệp rỗng)
    );

    // Kiểm tra xem phương thức ném ngoại lệ đúng hay không
    AppException exception = assertThrows(AppException.class, () -> hotelAmenitiesService.uploadIcon(1L, someMockFile));
    assertEquals("HotelId không tồn tại", exception.getMessage()); // Kiểm tra thông điệp ngoại lệ
}


}
