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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketService implements ITicketService {
    TicketRepository ticketRepository;
    TicketMapper ticketMapper;
    SeatRepository seatRepository;
    PaymentRepository paymentRepository;
    UserRepository userRepository;
    ScheduleRepository scheduleRepository;
    CouponRepository couponRepository;
    ScheduleSeatRepository scheduleSeatRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cancelUnpaidTickets() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        List<Ticket> unpaidTickets = ticketRepository.findPendingTicketsBefore(oneDayAgo);
        for (Ticket ticket : unpaidTickets) {
            ticket.setStatus(Status.Cancelled);
            ticketRepository.save(ticket);
        }
    }

//    @Scheduled(cron = "*/30 * * * * *")
//    @Transactional
//    public void ticketStatusIsCancelled() {
//        List<Ticket> tickets = ticketRepository.findByStatusCancelled();
//        for (Ticket ticket : tickets) {
//            List<Coupon> coupons = ticket.getCoupons();
//            if(coupons.size() != 0){
//                Coupon coupon = coupons.get(0);
//                coupon.setUseCount(coupon.getUseCount() - 1);
//                if (coupon.isActive() == false) {
//                    coupon.setActive(true);
//                } else {
//                    coupon.setActive(false);
//                }
//                coupons.set(0, coupon);
//                couponRepository.saveAll(coupons);
//                ticket.setCoupons(coupons);
//                ticketRepository.delete(ticket);
//            }
//        }
//    }

    @Override
    @Transactional
    public TicketResponse createTicket(TicketDTO ticketDto) throws Exception {
        User existingUser = userRepository.findByUsername(ticketDto.getUserName());
        if (existingUser == null) {
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
      
        List<Seat> seats = validateAndUpdateSeats(ticketDto.getSeatIds(), ticketDto.getScheduleId());
        ticket.setSeats(seats);

        if (coupon != null) {
            applyCouponDiscount(ticket, coupon);
        }

        ticket.setTotalPrice(ticket.getTotalPrice().subtract(BigDecimal.valueOf(payment.getFee())));
//        ticket.setTotalPrice(ticket.getTotalPrice());
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
                        () -> new AppException(ErrorType.notFound));
        User existingUser = userRepository.findByUsername(ticketDto.getUserName());
        if (existingUser == null) {
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

        List<Seat> seats = validateAndUpdateSeats(ticketDto.getSeatIds(), ticketDto.getScheduleId());
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
                        () -> new Exception("Ticket not found"));
        return ticketMapper.toResponse(ticketExisting);
    }

    @Override
    @Transactional
    public void deleteTicketById(Integer id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        ticket.ifPresent(ticket1 -> {
            if (ticket1.getStatus() == Status.Cancelled) {
                ticketRepository.delete(ticket1);
            }
        });
    }

    @Override
    public List<TicketResponse> findByUserId(Long id) {
        User existingUser = userRepository.findByIdActive(id);
        if(existingUser == null){
            throw new AppException(ErrorType.notFound);
        }
        return ticketRepository.findByUser_Id(existingUser.getId()).stream()
                .map(ticket -> ticketMapper.toResponse(ticket))
                .collect(Collectors.toList());
    }
    @Override
    public List<TicketResponse> findByScheduleId(Integer id) {
        Schedule existingUser = scheduleRepository.findById(id).orElseThrow(
                ()->new AppException(ErrorType.notFound)
        );
        return ticketRepository.findBySchedule_Id(existingUser.getId()).stream()
                .map(ticket -> ticketMapper.toResponse(ticket))
                .collect(Collectors.toList());
    }

    private Coupon getCoupon(String codeCoupon) throws Exception {
        if (codeCoupon != null && !codeCoupon.isEmpty()) {
            Coupon coupon = couponRepository.findByCode(codeCoupon);
            if (coupon == null) {
                throw new AppException(ErrorType.notFound);
            }
            if (!coupon.isActive()) {
                throw new AppException(ErrorType.couponIsExpired);
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

    private List<Seat> validateAndUpdateSeats(List<Integer> seatIds, Integer scheduleId) throws Exception {
        List<Seat> seats = (seatIds == null) ? new ArrayList<>() : seatRepository.findAllById(seatIds);
        if (seats.isEmpty()) {
            throw new Exception("Vui lòng chọn ghế");
        }

        StringBuilder sb = new StringBuilder();
        for (Seat seat : seats) {
            ScheduleSeat scheduleSeat = new ScheduleSeat();
            scheduleSeat = scheduleSeatRepository
                    .findByScheduleIdAndSeatId(scheduleId, seat.getId())
                    .orElseThrow(() -> new Exception(String.format("Không tìm thấy ghế %s trong lịch trình", seat.getSeatNumber())));

            if (scheduleSeat.getStatus() == StatusSeat.Full) {
                sb.append(String.format("Ghế số %s - mã số xe %s đã có người ngồi trong lịch trình này\n",
                        seat.getSeatNumber(), seat.getVehicle().getCode()));
            } else {
                scheduleSeat.setStatus(StatusSeat.Full);
                scheduleSeatRepository.save(scheduleSeat);
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
            discountPrice = ticket.getTotalPrice()
                    .multiply(coupon.getDiscountValue().multiply(BigDecimal.valueOf(0.01)));
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
