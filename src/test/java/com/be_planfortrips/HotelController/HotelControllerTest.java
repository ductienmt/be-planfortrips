package com.be_planfortrips.HotelController;
import com.be_planfortrips.controllers.HotelController;
import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.HotelListResponse;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.services.interfaces.IHotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    @InjectMocks
    private HotelController hotelController;

    @Mock
    private IHotelService iHotelService;

    @Mock
    private BindingResult bindingResult;

    private HotelDto hotelDto;
    private HotelResponse hotelResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hotelDto = HotelDto.builder()
                .address("123 Main St")
                .name("Test Hotel")
                .phoneNumber("123456789")
                .description("A nice place to stay")
                .build();
        hotelResponse = new HotelResponse();
    }

    @Test
    void createHotel_ShouldReturnOk_WhenValid() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(iHotelService.createHotel(hotelDto)).thenReturn(hotelResponse);

        ResponseEntity<?> response = hotelController.createHotel(hotelDto, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<HotelResponse> apiResponse = (ApiResponse<HotelResponse>) response.getBody();
        assertEquals(hotelResponse, apiResponse.getData());
        assertEquals("Tạo khách sạn thành công.", apiResponse.getMessage());
    }

    @Test
    void createHotel_ShouldReturnBadRequest_WhenValidationFails() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("hotelDto", "name", "Name hotel is required")));

        ResponseEntity<?> response = hotelController.createHotel(hotelDto, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<String> errors = (List<String>) response.getBody();
        assertEquals(1, errors.size());
        assertEquals("Name hotel is required", errors.get(0));
    }

    @Test
    void updateHotel_ShouldReturnOk_WhenValid() throws Exception {
        Long hotelId = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);
        when(iHotelService.updateHotel(hotelId, hotelDto)).thenReturn(hotelResponse);

        ResponseEntity<?> response = hotelController.updateHotel(hotelDto, hotelId, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<HotelResponse> apiResponse = (ApiResponse<HotelResponse>) response.getBody();
        assertEquals(hotelResponse, apiResponse.getData());
        assertEquals("Cập nhật khách sạn thành công.", apiResponse.getMessage());
    }

    @Test
    void getHotels_ShouldReturnOk() {
        List<HotelResponse> mockHotels = List.of(hotelResponse);
        Page<HotelResponse> pageResponse = mock(Page.class);
        when(pageResponse.toList()).thenReturn(mockHotels);
        when(iHotelService.searchHotels(any(PageRequest.class), any(), any())).thenReturn(pageResponse);
        when(pageResponse.getTotalPages()).thenReturn(1);

        ResponseEntity<HotelListResponse> response = hotelController.getHotels(0, 10, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        HotelListResponse hotelListResponse = response.getBody();
        assertEquals(mockHotels.size(), hotelListResponse.getHotelResponseList().size());
        assertEquals(1, hotelListResponse.getTotalPage());
    }

    @Test
    void deleteHotelById_ShouldReturnNoContent_WhenSuccessful() {
        Long hotelId = 1L;
        doNothing().when(iHotelService).deleteHotelById(hotelId);

        ResponseEntity<Void> response = hotelController.deleteHotelById(hotelId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(iHotelService, times(1)).deleteHotelById(hotelId);
    }

    @Test
    void getHotelById_ShouldReturnOk_WhenFound() throws Exception {
        Long hotelId = 1L;
        when(iHotelService.getByHotelId(hotelId)).thenReturn(hotelResponse);

        ResponseEntity<?> response = hotelController.getHotelById(hotelId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<HotelResponse> apiResponse = (ApiResponse<HotelResponse>) response.getBody();
        assertEquals(hotelResponse, apiResponse.getData());
    }

    @Test
    void uploadImages_ShouldReturnOk_WhenSuccessful() throws Exception {
        Long hotelId = 1L;
        List<MultipartFile> files = Collections.emptyList(); // Mock the file upload
        when(iHotelService.createHotelImage(hotelId, files)).thenReturn(hotelResponse);

        ResponseEntity<?> response = hotelController.uploadImages(hotelId, files);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<HotelResponse> apiResponse = (ApiResponse<HotelResponse>) response.getBody();
        assertEquals(hotelResponse, apiResponse.getData());
    }

    // Thêm các test cho các phương thức khác ở đây
}