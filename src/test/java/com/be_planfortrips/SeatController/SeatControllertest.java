package com.be_planfortrips.SeatController;

import com.be_planfortrips.controllers.SeatController;
import com.be_planfortrips.dto.SeatDTO;
import com.be_planfortrips.dto.response.SeatResponse;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.services.interfaces.ISeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatControllertest {

    @Mock
    private ISeatService iSeatService;

    @InjectMocks
    private SeatController seatController;

    private SeatDTO seatDTO;
    private SeatResponse seatResponse;

    @BeforeEach
    void setUp() {
        seatDTO = SeatDTO.builder()
                .vehicleCode("vehicleXYZ")
                .seatNumber("A1")
                .build();

        seatResponse = SeatResponse.builder()
                .id(1)
                .seatNumber("A1")
                .status("available")
                .build();
    }

    @Test
    void testCreateSeat()throws Exception {
        // Mocking the service layer
        when(iSeatService.createSeat(seatDTO)).thenReturn(seatResponse);

        // Simulating a successful seat creation
        ResponseEntity<?> response = seatController.createSeat(seatDTO, mock(BindingResult.class));

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seatResponse, response.getBody());
        verify(iSeatService).createSeat(seatDTO);
    }

    @Test
    void testUpdateSeat()throws Exception  {
        Integer seatId = 1;
        when(iSeatService.updateSeat(seatId, seatDTO)).thenReturn(seatResponse);

        ResponseEntity<?> response = seatController.updateSeat(seatId, seatDTO, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seatResponse, response.getBody());
        verify(iSeatService).updateSeat(seatId, seatDTO);
    }

    @Test
    void testGetAllSeats() {
        List<SeatResponse> seatList = Collections.singletonList(seatResponse);
        Page<SeatResponse> seatPage = new PageImpl<>(seatList);
        when(iSeatService.getSeats(PageRequest.of(0, 10))).thenReturn(seatPage);

        ResponseEntity<?> response = seatController.getCarCompanies(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        TListResponse<SeatResponse> listResponse = (TListResponse<SeatResponse>) response.getBody();
        assertEquals(1, listResponse.getListResponse().size());
        assertEquals(seatList, listResponse.getListResponse());
        assertEquals(seatPage.getTotalPages(), listResponse.getTotalPage());
        verify(iSeatService).getSeats(PageRequest.of(0, 10));
    }

    @Test
    void testDeleteSeatById() throws Exception {
        Integer seatId = 1;

        ResponseEntity<?> response = seatController.deleteCarCompanyById(seatId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(iSeatService).deleteSeatById(seatId);
    }

    @Test
    void testGetSeatById() throws Exception {
        Integer seatId = 1;
        when(iSeatService.getBySeatId(seatId)).thenReturn(seatResponse);

        ResponseEntity<?> response = seatController.getCarCompanyById(seatId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seatResponse, response.getBody());
        verify(iSeatService).getBySeatId(seatId);
    }

    @Test
    void testCreateSeatValidationError() throws Exception {
        // Simulate a validation error
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = seatController.createSeat(seatDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(iSeatService, never()).createSeat(seatDTO);
    }

    @Test
    void testUpdateSeatValidationError() throws Exception{
        Integer seatId = 1;
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = seatController.updateSeat(seatId, seatDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(iSeatService, never()).updateSeat(seatId, seatDTO);
    }
}