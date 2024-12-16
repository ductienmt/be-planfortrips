package com.be_planfortrips.RouteController;

import com.be_planfortrips.controllers.RouteController;
import com.be_planfortrips.dto.RouteDTO;
import com.be_planfortrips.dto.response.RouteResponse;
import com.be_planfortrips.services.interfaces.IRouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RouteControllerTest {

    @Mock
    private IRouteService iRouteService;

    @InjectMocks
    private RouteController routeController;

    private RouteDTO routeDTO;
    private RouteResponse routeResponse;

    @BeforeEach
    void setUp() {
        // Assuming RouteDTO requires a String, Integer, and Integer
        routeDTO = new RouteDTO("origin", 1, 2); // Replace with actual constructor parameters
        routeResponse = new RouteResponse(); // Initialize with necessary fields
    }

    @Test
    void testCreateRoute_Success() throws Exception {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(iRouteService.createRoute(routeDTO)).thenReturn(routeResponse);

        ResponseEntity<?> response = routeController.createRoute(routeDTO, result);

        assertEquals(ResponseEntity.ok(routeResponse), response);
        verify(iRouteService).createRoute(routeDTO);
    }

    @Test
    void testCreateRoute_BindingErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        when(result.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("field", "error", "Error message")));

        ResponseEntity<?> response = routeController.createRoute(routeDTO, result);

        assertEquals(ResponseEntity.badRequest().body(List.of("Error message")), response);
    }

    @Test
    void testUpdateRoute_Success() throws Exception{
        String code = "routeCode";
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(iRouteService.updateRoute(code, routeDTO)).thenReturn(routeResponse);

        ResponseEntity<?> response = routeController.updateRoute(code, routeDTO, result);

        assertEquals(ResponseEntity.ok(routeResponse), response);
        verify(iRouteService).updateRoute(code, routeDTO);
    }

    @Test
    void testDeleteRouteById_Success() throws Exception {
        String id = "routeId";

        ResponseEntity<?> response = routeController.deleteCarCompanyById(id);

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(iRouteService).deleteRouteById(id);
    }

    @Test
    void testGetRouteById_Success() throws Exception {
        String id = "routeId";
        when(iRouteService.getByRouteId(id)).thenReturn(routeResponse);

        ResponseEntity<?> response = routeController.getCarCompanyById(id);

        assertEquals(ResponseEntity.ok(routeResponse), response);
        verify(iRouteService).getByRouteId(id);
    }

    @Test
    void testGetCityByRouteId_Success() {
        String routeId = "routeId";
        // Mock expected return value and implement the test
    }
}