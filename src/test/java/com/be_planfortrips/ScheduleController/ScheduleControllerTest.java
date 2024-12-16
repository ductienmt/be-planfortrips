package com.be_planfortrips.ScheduleController;

import com.be_planfortrips.controllers.ScheduleController;
import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.request.DataSchedule;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.entity.ScheduleSeat;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.services.interfaces.IScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleControllerTest {

    @Mock
    private IScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    private ScheduleDto scheduleDto;
    private ScheduleResponse scheduleResponse;

    @BeforeEach
    void setUp() {
        // Initialize ScheduleDto with valid parameters
        scheduleDto = ScheduleDto.builder()
                .routeId("route123")
                .vehicleCode("vehicleXYZ")
                .driverName("John Doe")
                .driverPhone("+1234567890")
                .priceForOneSeat(BigDecimal.valueOf(99.99))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .build();

        // Initialize ScheduleResponse with valid parameters
        scheduleResponse = ScheduleResponse.builder()
                .Id(1)
                .routeId("route123")
                .code("scheduleXYZ")
                .carCompanyName("Best Cars")
                .priceForOneTicket(BigDecimal.valueOf(99.99))
                .carCompanyRating(BigDecimal.valueOf(4.5))
                .countSeatsEmpty(10L)
                .departureName("City A")
                .arrivalName("City B")
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .scheduleSeat(Collections.emptyList()) // Initialize with empty or valid data
                .build();
    }

    @Test
    void testGetAllSchedules() {
        List<ScheduleResponse> responses = Collections.singletonList(scheduleResponse);
        when(scheduleService.getAllSchedule()).thenReturn(responses);

        ResponseEntity<List<ScheduleResponse>> response = scheduleController.getAllSchedules();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responses, response.getBody());
        verify(scheduleService).getAllSchedule();
    }

    @Test
    void testGetScheduleById() {
        Integer scheduleId = 1;
        when(scheduleService.getScheduleById(scheduleId)).thenReturn(scheduleResponse);

        ResponseEntity<ScheduleResponse> response = scheduleController.getScheduleById(scheduleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scheduleResponse, response.getBody());
        verify(scheduleService).getScheduleById(scheduleId);
    }

    @Test
    void testCreateSchedule() {
        when(scheduleService.createSchedule(scheduleDto)).thenReturn(scheduleResponse);

        ResponseEntity<ScheduleResponse> response = scheduleController.createSchedule(scheduleDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(scheduleResponse, response.getBody());
        verify(scheduleService).createSchedule(scheduleDto);
    }

    @Test
    void testUpdateSchedule() {
        Integer scheduleId = 1;
        when(scheduleService.updateSchedule(scheduleDto, scheduleId)).thenReturn(scheduleResponse);

        ResponseEntity<ScheduleResponse> response = scheduleController.updateSchedule(scheduleId, scheduleDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scheduleResponse, response.getBody());
        verify(scheduleService).updateSchedule(scheduleDto, scheduleId);
    }

    @Test
    void testDeleteScheduleById() {
        Integer scheduleId = 1;

        ResponseEntity<Void> response = scheduleController.deleteScheduleById(scheduleId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(scheduleService).deleteScheduleById(scheduleId);
    }

    @Test
    void testGetStationByScheduleId() {
        Long scheduleId = 1L;
        Map<String, Object> mockResponse = Map.of("station", "Station A");
        when(scheduleService.getRouteByScheduleId(scheduleId)).thenReturn(mockResponse);

        ResponseEntity<?> response = scheduleController.getStationByScheduleId(scheduleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals(mockResponse, apiResponse.getData());
        verify(scheduleService).getRouteByScheduleId(scheduleId);
    }

    @Test
    void testGetSchedules() {
        DataSchedule dataSchedule = new DataSchedule(); // Initialize as needed
        List<ScheduleResponse> mockResponses = Collections.singletonList(scheduleResponse);
        when(scheduleService.getSchedules(dataSchedule)).thenReturn(mockResponses);

        ResponseEntity<?> response = scheduleController.getSchedules(dataSchedule);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals(mockResponses, apiResponse.getData());
        verify(scheduleService).getSchedules(dataSchedule);
    }

    @Test
    void testGetScheduleByTime() {
        LocalDateTime departureTime = LocalDateTime.now();
        LocalDateTime returnTime = LocalDateTime.now().plusHours(1);
        String originalLocation = "A";
        String destination = "B";
        Map<String, Object> mockResponse = Map.of("schedules", List.of(scheduleResponse));
        when(scheduleService.getAllScheduleByTime(departureTime, returnTime, originalLocation, destination)).thenReturn(mockResponse);

        ResponseEntity<?> response = scheduleController.getScheduleByTime(departureTime, returnTime, originalLocation, destination);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals(mockResponse, apiResponse.getData());
        verify(scheduleService).getAllScheduleByTime(departureTime, returnTime, originalLocation, destination);
    }
}