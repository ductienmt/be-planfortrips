package com.be_planfortrips.Test.anhquan;

import com.be_planfortrips.controllers.TourController;
import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.services.interfaces.ITourService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.validation.BindingResult;
import static org.mockito.Mockito.*;

class TourControllerTest {

    @InjectMocks
    private TourController tourController;

    @Mock
    private ITourService iTourService;

    public TourControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTours() throws Exception {
        // Mock dữ liệu trả về từ iTourService
        PageRequest request = PageRequest.of(0, 10);
        TourResponse mockTourResponse = new TourResponse(); // Cần set dữ liệu mẫu vào đây
        Page<TourResponse> mockPage = new PageImpl<>(List.of(mockTourResponse));

        when(iTourService.getActiveTours(request, "Test", null, List.of())).thenReturn(mockPage);

        // Gọi phương thức cần test
        ResponseEntity<?> response = tourController.getAllTours(0, 10, "Test", null, null);

        // Kiểm tra kết quả
        assertEquals(200, response.getStatusCode().value());
        TListResponse<?> body = (TListResponse<?>) response.getBody();
        assertEquals(1, body.getTotalPage());
        verify(iTourService, times(1)).getActiveTours(request, "Test", null, List.of());
    }

    @Test
    void testCreateTour() throws Exception {
        // Mock dữ liệu
        TourDTO tourDTO = new TourDTO(); // Cần set dữ liệu mẫu vào đây
        TourResponse mockResponse = new TourResponse();
        when(iTourService.createTour(tourDTO)).thenReturn(mockResponse);

        // Mock BindingResult
        BindingResult mockBindingResult = Mockito.mock(BindingResult.class);
        when(mockBindingResult.hasErrors()).thenReturn(false);  // or true if there are errors

        // Gọi phương thức cần test
        ResponseEntity<?> response = tourController.createTour(tourDTO, "tag1,tag2", mockBindingResult);

        // Kiểm tra kết quả
        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockResponse, response.getBody());
        verify(iTourService, times(1)).createTour(tourDTO);
    }

    @Test
    void testDeleteTour() throws Exception {
        // Gọi phương thức cần test
        ResponseEntity<?> response = tourController.deleteTour(1);

        // Kiểm tra kết quả
        assertEquals(204, response.getStatusCode().value());
        verify(iTourService, times(1)).deleteTourById(1);
    }
}
