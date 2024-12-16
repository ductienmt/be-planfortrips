package com.be_planfortrips.Area;

import com.be_planfortrips.controllers.AreaController;
import com.be_planfortrips.dto.AreaDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.AreaResponse;
import com.be_planfortrips.services.interfaces.IAreaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AreaControllerTest {

    @Mock
    private IAreaService areaService;

    @InjectMocks
    private AreaController areaController;

    private AreaDto mockDto;
    private AreaResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockDto = new AreaDto();
        mockDto.setId("MB");
        mockDto.setName("Miền Bắc");
        mockDto.setDescription("Mô tả");

        mockResponse = new AreaResponse();
        mockResponse.setId("1");
        mockResponse.setName("Miền Bắc");
        mockResponse.setDescription("Mô tả");
    }

    @Test
    void getAllAreasAll() {
        List<AreaResponse> mockResponses = List.of(mockResponse, mockResponse);
        when(areaService.getAll()).thenReturn(mockResponses);

        ResponseEntity<?> response = areaController.getAllAreas();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(mockResponses, apiResponse.getData());
        assertEquals("Lấy danh sách khu vực thành công.", apiResponse.getMessage());

        verify(areaService, times(1)).getAll();
    }

    @Test
    void getAllAreasNotFound() {
        when(areaService.getAll()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = areaController.getAllAreas();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Không có khu vực.", apiResponse.getMessage());

        verify(areaService, times(1)).getAll();
    }

    @Test
    void createArea() {
        when(areaService.createArea(mockDto)).thenReturn(mockResponse);
        ResponseEntity<?> response = areaController.createArea(mockDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(mockResponse, apiResponse.getData());
        assertEquals("Tạo khu vực thành công.", apiResponse.getMessage());

        verify(areaService, times(1)).createArea(mockDto);
    }

    @Test
    void createAreaInternalError() {
        when(areaService.createArea(mockDto)).thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<?> response = areaController.createArea(mockDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Tạo khu vực thất bại.", apiResponse.getMessage());

        verify(areaService, times(1)).createArea(mockDto);
    }

    @Test
    void updateArea() {
        String id = "1";
        when(areaService.updateArea(id, mockDto)).thenReturn(mockResponse);

        ResponseEntity<?> response = areaController.updateArea(id, mockDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(mockResponse, apiResponse.getData());
        assertEquals("Cập nhật khu vực thành công.", apiResponse.getMessage());

        verify(areaService, times(1)).updateArea(id, mockDto);
    }

    @Test
    void updateAreaInternalError() {
        String id = "1";
        when(areaService.updateArea(id, mockDto)).thenThrow(new RuntimeException("Update failed"));

        ResponseEntity<?> response = areaController.updateArea(id, mockDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Cập nhật khu vực thất bại.", apiResponse.getMessage());

        verify(areaService, times(1)).updateArea(id, mockDto);
    }

    @Test
    void deleteArea() {
        String id = "1";
        doNothing().when(areaService).deleteArea(id);

        ResponseEntity<?> response = areaController.deleteArea(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Xóa khu vực thành công.", apiResponse.getMessage());

        verify(areaService, times(1)).deleteArea(id);
    }

    @Test
    void deleteAreaWithEmptyId() {
        String id = "";
        ResponseEntity<?> response = areaController.deleteArea(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Xóa Thất Bại", apiResponse.getMessage());
        verify(areaService, never()).deleteArea(anyString());
    }
}