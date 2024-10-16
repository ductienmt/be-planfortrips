package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TicketDTO;
import com.be_planfortrips.dto.response.TicketResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
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

import java.math.BigDecimal;
import java.util.*;

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
    CouponRepository couponRepository;
    @Override
    @Transactional
    public TicketResponse createTicket(TicketDTO ticketDto) throws Exception {
        User existingUser = userRepository.findByUsername(ticketDto.getUserName());
        if(existingUser==null){
            throw new Exception("User not found");
        }
        Payment payment = paymentRepository.findById(ticketDto.getPaymentId())
                .orElseThrow(() -> new Exception("Vui lòng chọn phương thức thanh toán"));

        Schedule schedule = scheduleRepository.findById(ticketDto.getScheduleId())
                .orElseThrow(() -> new Exception("Vui lòng chọn lịch trình"));

        Coupon coupon = getCoupon(ticketDto.getCodeCoupon());

        Ticket ticket = ticketMapper.toEntity(ticketDto);
        ticket.setPayment(payment);
        ticket.setUser(existingUser);
        ticket.setSchedule(schedule);

        validateTicketStatus(ticketDto.getStatus());

        List<Seat> seats = validateAndUpdateSeats(ticketDto.getSeatIds());
        ticket.setSeats(seats);

        if (coupon != null) {
            applyCouponDiscount(ticket, coupon);
        }

        ticket.setTotalPrice(ticket.getTotalPrice().subtract(BigDecimal.valueOf(payment.getFee())));
        ticket.setStatus(Status.Pending);

        ticketRepository.saveAndFlush(ticket);

        updateCouponUsage(coupon);

        return ticketMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse updateTicket(Integer id, TicketDTO ticketDto) throws Exception {
        Ticket ticketExisting = ticketRepository.findById(id)
                .orElseThrow(
                        ()-> new AppException(ErrorType.notFound, "")
                );
        User existingUser = userRepository.findByUsername(ticketDto.getUserName());
        if(existingUser==null){
            throw new Exception("User not found");
        }
        Payment payment = paymentRepository.findById(ticketDto.getPaymentId())
                .orElseThrow(() -> new Exception("Vui lòng chọn phương thức thanh toán"));

        Schedule schedule = scheduleRepository.findById(ticketDto.getScheduleId())
                .orElseThrow(() -> new Exception("Vui lòng chọn lịch trình"));

        Coupon coupon = getCoupon(ticketDto.getCodeCoupon());

        ticketExisting = ticketMapper.toEntity(ticketDto);
        ticketExisting.setPayment(payment);
        ticketExisting.setUser(existingUser);
        ticketExisting.setSchedule(schedule);

        validateTicketStatus(ticketDto.getStatus());

        List<Seat> seats = validateAndUpdateSeats(ticketDto.getSeatIds());
        ticketExisting.setSeats(seats);

        if (coupon != null) {
            applyCouponDiscount(ticketExisting, coupon);
        }

        ticketExisting.setTotalPrice(ticketExisting.getTotalPrice().subtract(BigDecimal.valueOf(payment.getFee())));
        ticketExisting.setStatus(Status.valueOf(ticketDto.getStatus()));

        ticketRepository.saveAndFlush(ticketExisting);

        updateCouponUsage(coupon);

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
    private Coupon getCoupon(String codeCoupon) throws Exception {
        if (codeCoupon != null && !codeCoupon.isEmpty()) {
            Coupon coupon = couponRepository.findByCode(codeCoupon);
            if(coupon==null){
                throw new AppException(ErrorType.notFound, "");
            }
            if (!coupon.isActive()) {
                throw new AppException(ErrorType.couponIsExpired, "");
            }
            return coupon;
        }
        return null;
    }

    private void validateTicketStatus(String status) throws Exception {
        List<String> allowedStatuses = Arrays.asList("PENDING", "CANCELED", "COMPLETED");
        if (!allowedStatuses.contains(status)) {
            throw new Exception("Trạng thái không hợp lệ");
        }
    }

    private List<Seat> validateAndUpdateSeats(List<Integer> seatIds) throws Exception {
        List<Seat> seats = (seatIds == null) ? new ArrayList<>() : seatRepository.findAllById(seatIds);
        if (seats.isEmpty()) {
            throw new Exception("Vui lòng chọn ghế");
        }

        StringBuilder sb = new StringBuilder();
        for (Seat seat : seats) {
            if (seat.getStatus() == StatusSeat.Full) {
                sb.append(String.format("Ghế số %s - mã số xe %s đã có người ngồi \n", seat.getSeatNumber(), seat.getVehicleCode().getCode()));
            } else {
                seat.setStatus(StatusSeat.Full);
                seatRepository.saveAndFlush(seat);
            }
        }

        if (sb.length() > 0) {
            throw new Exception(String.format("Lỗi: %s", sb));
        }

        return seats;
    }

    private void applyCouponDiscount(Ticket ticket, Coupon coupon) {
        BigDecimal discountPrice = BigDecimal.ZERO;
        if (coupon.getDiscountType() == DiscountType.PERCENT) {
            discountPrice = ticket.getTotalPrice().multiply(coupon.getDiscountValue().multiply(BigDecimal.valueOf(0.01)));
        } else {
            discountPrice = coupon.getDiscountValue();
        }
        ticket.setTotalPrice(ticket.getTotalPrice().subtract(discountPrice));
        ticket.setCoupons(Collections.singletonList(coupon));
    }

    private void updateCouponUsage(Coupon coupon) {
        if (coupon != null && coupon.getUseCount() < coupon.getUseLimit()) {
            coupon.setUseCount(coupon.getUseCount() + 1);
            if (coupon.getUseCount() == coupon.getUseLimit()) {
                coupon.setActive(false);
            }
            couponRepository.saveAndFlush(coupon);
        }
    }
}
