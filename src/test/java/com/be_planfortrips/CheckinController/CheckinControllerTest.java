package com.be_planfortrips.CheckinController;

import com.be_planfortrips.controllers.CheckinController;
import com.be_planfortrips.dto.CheckinDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.CheckinResponse;
import com.be_planfortrips.services.interfaces.ICheckinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckinControllerTest {

    @Mock
    private ICheckinService checkinService;

    @InjectMocks
    private CheckinController checkinController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCheckin() {

        Map<String, Object> mockData = new HashMap<>();
        mockData.put("data", List.of(new CheckinResponse()));
        when(checkinService.getAllCheckin(1)).thenReturn(mockData);


        ResponseEntity<?> response = checkinController.getAllCheckin(1);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertNotNull(apiResponse);
        assertEquals("Lấy danh sách điểm checkin thành công.", apiResponse.getMessage());
        verify(checkinService, times(1)).getAllCheckin(1);
    }

    @Test
    void testGetAllCheckin_Failure() {

        when(checkinService.getAllCheckin(1)).thenThrow(new RuntimeException("Database error"));


        ResponseEntity<?> response = checkinController.getAllCheckin(1);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertNotNull(apiResponse);
        assertEquals("Lấy danh sách điểm checkin thất bại.", apiResponse.getMessage());
        verify(checkinService, times(1)).getAllCheckin(1);
    }

    @Test
    void testGetCheckin_Success() {

        Long checkinId = 1L;
        CheckinResponse mockCheckin = new CheckinResponse();
        when(checkinService.getCheckin(checkinId)).thenReturn(mockCheckin);


        ResponseEntity<?> response = checkinController.getCheckin(checkinId);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertNotNull(apiResponse);
        assertEquals("Lấy thông tin điểm checkin thành công.", apiResponse.getMessage());
        verify(checkinService, times(1)).getCheckin(checkinId);
    }

    @Test
    void testCreateCheckin_Success() {

        CheckinDto checkinDto = new CheckinDto();
        CheckinResponse mockResponse = new CheckinResponse();
        when(checkinService.createCheckin(checkinDto)).thenReturn(mockResponse);


        ResponseEntity<?> response = checkinController.createCheckin(checkinDto);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertNotNull(apiResponse);
        assertEquals("Tạo điểm checkin thành công.", apiResponse.getMessage());
        verify(checkinService, times(1)).createCheckin(checkinDto);
    }

    @Test
    void testDeleteCheckin_Success() {

        Long checkinId = 1L;
        doNothing().when(checkinService).deleteCheckin(checkinId);


        ResponseEntity<?> response = checkinController.deleteCheckin(checkinId);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertNotNull(apiResponse);
        assertEquals("Xóa điểm checkin thành công.", apiResponse.getMessage());
        verify(checkinService, times(1)).deleteCheckin(checkinId);
    }

    @Test
    void testUploadImage_Success() {
        // Given
        Long checkinId = 1L;
        List<MultipartFile> files = Collections.emptyList();
        doNothing().when(checkinService).uploadImage(checkinId, files);


        ResponseEntity<?> response = checkinController.uploadImage(checkinId, files);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertNotNull(apiResponse);
        assertEquals("Upload ảnh thành công.", apiResponse.getMessage());
        verify(checkinService, times(1)).uploadImage(checkinId, files);
    }
}
