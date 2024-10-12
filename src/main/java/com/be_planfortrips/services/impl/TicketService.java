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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    @Transactional
    public TicketResponse createTicket(TicketDTO ticketDto) throws Exception {
        StringBuilder sb = new StringBuilder();
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
        List<String> allowedStatuses = Arrays.asList("PENDING", "CANCELED", "COMPLETED");
        if (!allowedStatuses.contains(ticketDto.getStatus())) {
            throw new Exception("Trang thai khong hop le");
        }
        List<Integer> seatIds = ticketDto.getSeatIds();
        List<Seat> seats = (seatIds==null) ? new ArrayList<>() : seatRepository.findAllById(seatIds);
        if(seats.isEmpty()){
            throw new Exception("Vui lòng chọn ghees");
        }
        for(int i = 0;i<seats.size();i++){
            Seat seat = seats.get(i);
            if(seat.getStatus() == StatusSeat.Full){
                sb.append(String.format("Ghế so' %s - mã số xe %s da co nguoi ngoi` \n",seat.getSeatNumber(),seat.getVehicleCode().getCode()));
            }else{
                seat.setStatus(StatusSeat.Full);
                seatRepository.saveAndFlush(seat);
                seats.set(i,seat);
            }
        }
        if(!sb.toString().isEmpty()) throw new Exception(String.format("Lỗi: %s", sb)); else ticket.setSeats(seats);
        ticket.setStatus(Status.Pending);
        ticketRepository.saveAndFlush(ticket);
        return ticketMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse updateTicket(Integer id, TicketDTO ticketDto) throws Exception {
        StringBuilder sb = new StringBuilder();
        Ticket ticketExisting = ticketRepository.findById(id)
                .orElseThrow(
                        ()-> new Exception("Ticket not found")
                );
        if (ticketExisting == null) {
            return null;
        }
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
        List<Integer> seatIds = ticketDto.getSeatIds();
        List<Seat> seats = (seatIds==null) ? new ArrayList<>() : seatRepository.findAllById(seatIds);
        if(seats.size() == 0){
            throw new Exception("Vui lòng chọn ghees");
        }
        for(int i = 0;i<seats.size();i++){
            Seat seat = seats.get(i);
            if(seat.getStatus() == StatusSeat.Empty || ticketDto.getSeatIds().contains(seat.getId())){
                seat.setStatus(StatusSeat.Full);
                seatRepository.saveAndFlush(seat);
                seats.set(i,seat);
            }else{
                sb.append(String.format("Ghế so' %s - mã số xe %s da co nguoi ngoi` \n",seat.getSeatNumber(),seat.getVehicleCode().getCode()));
            }
        }
        ticketExisting = ticketMapper.toEntity(ticketDto);
        ticketExisting.setId(id);
        ticketExisting.setPayment(payment);
        ticketExisting.setUser(existingUser);
        ticketExisting.setSchedule(schedule);
        List<String> allowedStatuses = Arrays.asList("PENDING", "CANCELED   ", "COMPLETED");
        if (!allowedStatuses.contains(ticketDto.getStatus())) {
            throw new Exception("Trang thai khong hop le");
        }
        if(!sb.toString().isEmpty()) throw new Exception(String.format("Lỗi: %s",sb.toString())); else ticketExisting.setSeats(seats);
        ticketRepository.saveAndFlush(ticketExisting);
        return ticketMapper.toResponse(ticketExisting);
    }

    @Override
    public Page<TicketResponse> getTickets(PageRequest request) {
        return ticketRepository.findAll(request).map(ticketMapper::toResponse);
    }

    @Override
    public TicketResponse getByTicketId(Integer id) throws Exception {
        Ticket ticketExisting = ticketRepository.findById(id)
                .orElseThrow(
                        ()-> new Exception("Ticket not found")
                );
        return ticketMapper.toResponse(ticketExisting);
    }

    @Override
    @Transactional
    public void deleteTicketById(Integer id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        ticket.ifPresent(ticketRepository::delete);
    }
}
