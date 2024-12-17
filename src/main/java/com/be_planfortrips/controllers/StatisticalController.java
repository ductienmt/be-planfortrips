package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.response.*;
import com.be_planfortrips.dto.sql.StatisticalCount;
import com.be_planfortrips.dto.sql.StatisticalCountMonth;
import com.be_planfortrips.dto.sql.StatisticalResource;
import com.be_planfortrips.dto.sql.StatisticalBookingHotelDetail;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.BookingHotelDetail;
import com.be_planfortrips.entity.Ticket;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.mappers.impl.AccountEnterpriseMapper;
import com.be_planfortrips.mappers.impl.BookingHotelDetailMapper;
import com.be_planfortrips.mappers.impl.TicketMapper;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.StatisticalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/statistical")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class StatisticalController {

    UserRepository userRepository;
    AdminRepository adminRepository;
    AccountEnterpriseRepository accountEnterpriseRepository;
    PlanRepository planRepository;
    PlanDetailRepository planDetailRepository;

    StatisticalService statisticalService;
    VehicleRepository vehicleRepository;
    private final HotelRepository hotelRepository;

    AccountEnterpriseMapper accountEnterpriseMapper;
    TicketRepository ticketRepository;
    BookingHotelDetailRepository bookingHotelDetailRepository;

    BookingHotelDetailMapper bookingHotelDetailMapper;

    TicketMapper ticketMapper;



    @GetMapping("/user")
    public ResponseEntity<Integer> getCountUser() {
        return ResponseEntity.ok(userRepository.countAll());
    }

    @GetMapping("/user/{year}")
    public ResponseEntity<List<StatisticalCount>> StatisticalUserByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalCount> res = userRepository.countUsersByYear(year);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/user/{year}/{month}")
    public ResponseEntity<List<StatisticalCount>> StatisticalUserByMonth(
            @PathVariable("year") Integer year, @PathVariable("month") Integer month
    ){
        List<StatisticalCount> res = userRepository.countUsersByMonth(year, month);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/admin")
    public ResponseEntity<Integer> getCountAdmin() {
        return ResponseEntity.ok(adminRepository.countAll());
    }

    @GetMapping("/enterprise")
    public ResponseEntity<Integer> getCountEnterprise() {
        return ResponseEntity.ok(accountEnterpriseRepository.countAll());
    }

    @GetMapping("/enterprise/{year}")
    public ResponseEntity<List<StatisticalCount>> StatisticalEtpByYear(
            @PathVariable Integer year
    ) {
        List<StatisticalCount> statisticalCountEtp = accountEnterpriseRepository.StatisticalCountEtpByYear(year);
        return ResponseEntity.ok(statisticalCountEtp);
    }


    @GetMapping("/plan")
    public ResponseEntity<Integer> getCountPlan() {
        return ResponseEntity.ok(planRepository.getCountPlan());
    }

    @GetMapping("/plan/{year}")
    public ResponseEntity<List<StatisticalCount>> StatisticalPlanByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalCount> res = planRepository.countPlansByYear(year);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/plan/{year}/{month}")
    public ResponseEntity<List<StatisticalCount>> StatisticalPlanByMonth(
            @PathVariable("year") Integer year,
            @PathVariable("month") Integer month
    ) {
        List<StatisticalCount> res = planRepository.StatisticalCountPlanByMonth(year, month);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/enterprise/{year}/{month}")
    public ResponseEntity<List<StatisticalCount>> StatisticalEtpByMonth(
            @PathVariable("year") Integer year, @PathVariable("month") Integer month
    ) {
        List<StatisticalCount> statisticalCountMonths = accountEnterpriseRepository.StatisticalCountEtpByMonth(year, month);
        return ResponseEntity.ok(statisticalCountMonths);
    }

    @GetMapping("/year/bookingHotelDetail/{year}")
    public ResponseEntity<List<StatisticalBookingHotelDetail>>
            statisticalBkdetailByYear(@PathVariable Integer year) {
        List<StatisticalBookingHotelDetail> res = statisticalService.statisticalBookingHotelByYear(year);
        return ResponseEntity.ok(res);
    }


    @GetMapping("/vehicle/{year}")
    public ResponseEntity<List<StatisticalResource>> StatisticalVehicleByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalResource> res = vehicleRepository.getTop1VehicleByYear(year);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/hotel/{year}")
    public ResponseEntity<List<StatisticalResource>> StatisticalHotelByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalResource> res = hotelRepository.getTop1HotelByYear(year);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/pdf/user")
    public ResponseEntity<List<UserResponse>> getDataExcelUser() {
        List<User> users = userRepository.findAll();

        // Chuyển danh sách User sang danh sách UserResponse
        List<UserResponse> userResponses = users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .address(user.getAddress())
                        .phoneNumber(user.getPhoneNumber())
                        .gender(user.getGender())
                        .isActive(user.isActive())
                        .birthdate(user.getBirthdate())
                        .build())
                .toList();

        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/pdf/enterppirse/{startTime}/{endTime}")
    public ResponseEntity<List<AccountEnterpriseResponse>> getDataExcelEnterprise(
            @PathVariable LocalDateTime startTime, @PathVariable LocalDateTime endTime) {

        // Lấy dữ liệu từ repository với khoảng thời gian được truyền vào
        List<AccountEnterprise> accountEnterprises = accountEnterpriseRepository.findByCreateAtBetween(startTime, endTime);

        // Chuyển đổi sang response object (AccountUserResponse)
        List<AccountEnterpriseResponse> response = accountEnterprises.stream()
                .map(accountEnterpriseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pdf/ticket/{startTime}/{endTime}")
    public ResponseEntity<List<TicketResponse>> getDataExcelTicket(
            @PathVariable LocalDateTime startTime,
            @PathVariable LocalDateTime endTime
    ) {
        List<Ticket> tickets = ticketRepository.findByCreateAtBetween(startTime, endTime);

        return ResponseEntity.ok(tickets.stream().map(ticketMapper::toResponse).toList());
    }

    @GetMapping("/pdf/bookingHotelDetail/{startTime}/{endTime}")
    public ResponseEntity<List<BookingHotelDetailResponse>> getDataExcelBhd(
            @PathVariable LocalDateTime startTime,
            @PathVariable LocalDateTime endTime
    ) {
        List<BookingHotelDetail> bookingHotelDetails =
                bookingHotelDetailRepository.findByCreateAtBetween(startTime, endTime);
        return ResponseEntity.ok(bookingHotelDetails.stream().map(bookingHotelDetailMapper::toResponse).toList());
    }


}
