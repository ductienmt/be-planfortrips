package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TicketDTO;
import com.be_planfortrips.dto.response.TicketResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.mappers.impl.TicketMapper;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.interfaces.ITicketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TicketService implements ITicketService {
    TicketRepository ticketRepository;
    TicketMapper ticketMapper;
    SeatRepository seatRepository;
    PaymentRepository paymentRepository;
    UserRepository userRepository;
    ScheduleRepository scheduleRepository;
    @Override
    public TicketResponse createTicket(TicketDTO ticketDto) throws Exception {
        User existingUser = userRepository.findByUsername(ticketDto.getUserName());
        if(existingUser==null){
            throw new Exception("User not found");
        }
        Payment payment = paymentRepository.findById(ticketDto.getPaymentId())
                .orElseThrow(
                        ()->new Exception("Vui lòng chọn phương thức thanh toán")
                );
        Schedule schedule = scheduleRepository.findById(ticketDto.getScheduleId())
                .orElseThrow(
                        ()-> new Exception("Vui long chon lich trinh")
                );
        Ticket ticket = ticketMapper.toEntity(ticketDto);
        ticket.setPayment(payment);
        ticket.setUser(existingUser);
        ticket.setSchedule(schedule);
        List<Integer> seatIds = ticketDto.getSeatIds();
        List<Seat> seats = (seatIds==null) ? new ArrayList<>() : seatRepository.findAllById(seatIds);
        if(seats.size() == 0){
            throw new Exception("Vui lòng chọn ghees");
        }
        ticket.setSeats(seats);
        ticketRepository.saveAndFlush(ticket);
        return ticketMapper.toResponse(ticket);
    }

    @Override
    public TicketResponse updateTicket(Integer id, TicketDTO TicketDto) throws Exception {
        return null;
    }

    @Override
    public Page<TicketResponse> getTickets(PageRequest request) {
        return null;
    }

    @Override
    public TicketResponse getByTicketId(Integer id) throws Exception {
        return null;
    }

    @Override
    public void deleteTicketById(Integer id) {

    }
}
