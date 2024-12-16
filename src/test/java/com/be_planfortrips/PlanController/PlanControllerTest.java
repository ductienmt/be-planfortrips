package com.be_planfortrips.PlanController;

import com.be_planfortrips.dto.PlanDto;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.services.interfaces.IPlanService;
import com.be_planfortrips.controllers.PlanController;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.PlanResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class PlanControllerTest {

    @Mock
    private IPlanService planService;

    @InjectMocks
    private PlanController planController;

    private PlanDto planDto;
    private DataEssentialPlan dataEssentialPlan;

    @BeforeEach
    void setUp() {

        planDto = PlanDto.builder()
                .planName("Test Plan")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .location("Test Location")
                .destination("Test Destination")
                .budget(BigDecimal.valueOf(1000))
                .numberPeople(5)
                .totalPrice(BigDecimal.valueOf(1500))
                .discountPrice(BigDecimal.valueOf(100))
                .finalPrice(BigDecimal.valueOf(1400))
                .planDetails(new ArrayList<>())
                .build();

        dataEssentialPlan = new DataEssentialPlan();
    }

    @Test
    void testSavePlan_Success() {
     //   when(planService.save(planDto)).thenReturn(null);
        ResponseEntity<?> response = planController.savePlan(planDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Lưu kế hoạch thành công", response.getBody());
        verify(planService, times(1)).save(planDto);
    }

    @Test
    void testGetAllPlanByUserId_Success() {
        List<PlanResponse> plans = List.of(new PlanResponse());
        when(planService.getAllPlanByUserId()).thenReturn(plans);
        ResponseEntity<?> response = planController.getAllPlanByUserId();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(plans, response.getBody());
        verify(planService, times(1)).getAllPlanByUserId();
    }

    @Test
    void testPrepareDataPlan() {
        Map<String, Object> mockResponse = Map.of("key", "value");
        when(planService.prepareDataPlan(dataEssentialPlan)).thenReturn(mockResponse);
        ResponseEntity<?> response = planController.prepareDataPlan(dataEssentialPlan);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<Map<String, Object>> apiResponse = (ApiResponse<Map<String, Object>>) response.getBody();
        assertNotNull(apiResponse);
        assertEquals(mockResponse, apiResponse.getData());
        verify(planService, times(1)).prepareDataPlan(dataEssentialPlan);
    }


}