package com.be_planfortrips.TicketController;

import com.be_planfortrips.controllers.TicketController;
import com.be_planfortrips.dto.TicketDTO;
import com.be_planfortrips.dto.response.TicketResponse;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.services.interfaces.ITicketService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketControllertest {

    @Mock
    private ITicketService iTicketService;

    @InjectMocks
    private TicketController ticketController;

    private TicketDTO ticketDTO;
    private TicketResponse ticketResponse;

    @BeforeEach
    void setUp() {
        ticketDTO = TicketDTO.builder()
                .scheduleId(1)
                .userName("John Doe")
                .totalPrice(BigDecimal.valueOf(100.00))
                .paymentId(1L)
                .status("confirmed")
                .build();

        ticketResponse = TicketResponse.builder()
                .ticketId(1)
                .user_id(1L)
                .totalPrice(BigDecimal.valueOf(100.00))
                .status("confirmed")
                .build();
    }

    @Test
    void testCreateTicket() throws Exception {
        String seatIds = "1,2,3";
        when(iTicketService.createTicket(ticketDTO)).thenReturn(ticketResponse);

        ResponseEntity<?> response = ticketController.createTicket(ticketDTO, seatIds, null, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketResponse, response.getBody());
        verify(iTicketService).createTicket(ticketDTO);
    }

    @Test
    void testUpdateTicket() throws Exception {
        Integer ticketId = 1;
        String seatIds = "1,2,3";
        when(iTicketService.updateTicket(ticketId, ticketDTO)).thenReturn(ticketResponse);

        ResponseEntity<?> response = ticketController.updateTicket(ticketId, ticketDTO, seatIds, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketResponse, response.getBody());
        verify(iTicketService).updateTicket(ticketId, ticketDTO);
    }

    @Test
    void testGetAllTickets() {
        List<TicketResponse> ticketList = Collections.singletonList(ticketResponse);
        Page<TicketResponse> ticketPage = new PageImpl<>(ticketList);
        when(iTicketService.getTickets(PageRequest.of(0, 10))).thenReturn(ticketPage);

        ResponseEntity<?> response = ticketController.getCarCompanies(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        TListResponse<TicketResponse> listResponse = (TListResponse<TicketResponse>) response.getBody();
        assertEquals(1, listResponse.getListResponse().size());
        assertEquals(ticketList, listResponse.getListResponse());
        assertEquals(ticketPage.getTotalPages(), listResponse.getTotalPage());
        verify(iTicketService).getTickets(PageRequest.of(0, 10));
    }

    @Test
    void testDeleteTicketById() throws Exception {
        Integer ticketId = 1;

        ResponseEntity<?> response = ticketController.deleteCarCompanyById(ticketId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(iTicketService).deleteTicketById(ticketId);
    }

    @Test
    void testGetTicketById() throws Exception {
        Integer ticketId = 1;
        when(iTicketService.getByTicketId(ticketId)).thenReturn(ticketResponse);

        ResponseEntity<?> response = ticketController.getCarCompanyById(ticketId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketResponse, response.getBody());
        verify(iTicketService).getByTicketId(ticketId);
    }

    @Test
    void testGetTicketsByUserId() {
        Long userId = 1L;
        List<TicketResponse> ticketList = Collections.singletonList(ticketResponse);
        when(iTicketService.findByUserId(userId)).thenReturn(ticketList);

        ResponseEntity<?> response = ticketController.getCarCompanyByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketList, response.getBody());
        verify(iTicketService).findByUserId(userId);
    }

    @Test
    void testGetTicketsByScheduleId() {
        Integer scheduleId = 1;
        List<TicketResponse> ticketList = Collections.singletonList(ticketResponse);
        when(iTicketService.findByScheduleId(scheduleId)).thenReturn(ticketList);

        ResponseEntity<?> response = ticketController.getCarCompanyByScheduleId(scheduleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketList, response.getBody());
        verify(iTicketService).findByScheduleId(scheduleId);
    }

    @Test
    void testCreateTicketValidationError() throws Exception {
        // Simulate a validation error
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = ticketController.createTicket(ticketDTO, "1,2", null, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(iTicketService, never()).createTicket(ticketDTO);
    }

    @Test
    void testUpdateTicketValidationError() throws Exception {
        Integer ticketId = 1;
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = ticketController.updateTicket(ticketId, ticketDTO, "1,2", bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(iTicketService, never()).updateTicket(ticketId, ticketDTO);
    }
}