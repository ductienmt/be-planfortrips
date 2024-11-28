package com.be_planfortrips.controllers;

import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.Plan;
import com.be_planfortrips.entity.Status;
import com.be_planfortrips.entity.Ticket;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.BookingHotelRepository;
import com.be_planfortrips.repositories.HotelRepository;
import com.be_planfortrips.repositories.PlanRepository;
import com.be_planfortrips.repositories.TicketRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/vietqr")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class VietQrController {


    BookingHotelRepository bookingHotelRepository;
    private final TicketRepository ticketRepository;
    private final PlanRepository planRepository;

    @PostMapping("/{bookingHotelId}")
    public ResponseEntity<?> paymentVietQr(
          @PathVariable("bookingHotelId") Long bookingHotelId
    ) {
        BookingHotel bookingHotel =
                bookingHotelRepository.findById(bookingHotelId).orElseThrow(
                        () -> new AppException(ErrorType.notFound)
                );
        bookingHotel.setStatus(Status.Complete);
        bookingHotel.getBookingHotelDetails().forEach((bk) -> {
            bk.setStatus(Status.Complete);
        });
        bookingHotelRepository.save(bookingHotel);
        return ResponseEntity.ok("OKKKKKK");
    }

    @PostMapping("/payment-transportation")
    public ResponseEntity<?> paymentTransportation(

            @RequestParam("ticketId") Integer departureTicketId
    ) {

        Ticket ticket = ticketRepository.findById(departureTicketId).orElseThrow(
                () -> new AppException(ErrorType.notFound, "Không tìm thấy vé với id: " + departureTicketId)
        );
        ticket.setStatus(Status.Complete);
        ticketRepository.save(ticket);

        return ResponseEntity.ok("Đặt vé thành công");
    }

    @PostMapping("/payment-plan")
    public ResponseEntity<?> paymentPlan(
            @RequestParam("planId") Long planId,
            @RequestParam("bookingHotelId") Long bookingHotelId,
            @RequestParam("departureTicketId") Integer departureTicketId,
            @RequestParam("returnTicketId") Integer returnTicketId
    ) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new AppException(ErrorType.notFound, "Không tìm thấy plan với id: " + planId)
        );
        plan.setStatusPayment(Status.Complete);
        planRepository.save(plan);
        BookingHotel bookingHotel =
                bookingHotelRepository.findById(bookingHotelId).orElseThrow(
                        () -> new AppException(ErrorType.notFound, "Không tìm thấy booking với id: " + bookingHotelId)
                );
        bookingHotel.setStatus(Status.Complete);
        bookingHotel.getBookingHotelDetails().forEach((bk) -> {
            bk.setStatus(Status.Complete);
        });
        bookingHotelRepository.save(bookingHotel);
        Ticket departureTicket = ticketRepository.findById(departureTicketId).orElseThrow(
                () -> new AppException(ErrorType.notFound, "Không tìm thấy vé với id: " + departureTicketId)
        );
        departureTicket.setStatus(Status.Complete);
        ticketRepository.save(departureTicket);
        Ticket returnTicket = ticketRepository.findById(returnTicketId).orElseThrow(
                () -> new AppException(ErrorType.notFound, "Không tìm thấy vé với id: " + returnTicketId)
        );
        returnTicket.setStatus(Status.Complete);
        ticketRepository.save(returnTicket);
        return ResponseEntity.ok("Đặt vé thành công");
    }
}
