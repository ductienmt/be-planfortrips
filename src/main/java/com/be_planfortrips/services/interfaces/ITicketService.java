package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.TicketDTO;
import com.be_planfortrips.dto.response.TicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ITicketService {
    TicketResponse createTicket(TicketDTO TicketDto) throws Exception;
    TicketResponse updateTicket(Integer id,TicketDTO TicketDto) throws Exception;
    Page<TicketResponse> getTickets(PageRequest request);
    TicketResponse getByTicketId(Integer id) throws Exception;
    void deleteTicketById(Integer id);
    List<TicketResponse> findByUserId(Long id);
    List<TicketResponse> findByScheduleId(Integer id);
}

