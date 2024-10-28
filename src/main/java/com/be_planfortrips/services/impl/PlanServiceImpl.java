package com.be_planfortrips.services.impl;

import com.be_planfortrips.controllers.PlanController;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.dto.response.*;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.repositories.BookingHotelRepository;
import com.be_planfortrips.repositories.CheckinRepository;
import com.be_planfortrips.repositories.PlanRepository;
import com.be_planfortrips.repositories.TicketRepository;
import com.be_planfortrips.services.interfaces.IPlanService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlanServiceImpl implements IPlanService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanDetailServiceImpl planDetailService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CheckinServiceImpl checkinService;

    @Autowired
    private ScheduleServiceImpl scheduleService;
    @Autowired
    private BookingHotelRepository bookingHotelRepository;
    @Autowired
    private CheckinRepository checkinRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Map<String, Object> prepareDataPlan(DataEssentialPlan dataEssentialPlan) {
        Map<String, Object> dataEssential = new HashMap<>();
        dataEssential.put("userData", dataEssentialPlan);
        List<CheckinResponse> checkinResponses = this.checkinService.getCheckinRandom(5);
        dataEssential.put("checkins", checkinResponses);
        Map<String, Object> schedulesResponse = this.scheduleService.getAllScheduleByTime(dataEssentialPlan.getStartDate(), dataEssentialPlan.getEndDate());
        dataEssential.put("schedules", schedulesResponse);
        Map<String, Object> hotels = this.hotelService.getRoomAvailable(dataEssentialPlan.getNumberPeople(), dataEssentialPlan.getStartDate(), dataEssentialPlan.getEndDate());
        dataEssential.put("hotels", hotels);
        return dataEssential;
    }

    @Override
    public List<PlanResponse> getAllPlan() {
        return List.of();
    }

    @Override
    public List<PlanResponse> getAllPlanByUserId(Long userId) {
        List<Plan> plans = planRepository.findAllByUserId(userId);

        return plans.stream().map(
                plan -> {
                    PlanResponse planResponse = new PlanResponse();
                    planResponse.setPlan_id(Long.valueOf(plan.getId()));
                    planResponse.setPlan_name(plan.getPlanName());
                    planResponse.setBudget(plan.getBudget());
                    planResponse.setNumberPeople(plan.getNumberPeople());
                    return planResponse;
                }).collect(Collectors.toList());
    }

    @Override
    public PlanResponseDetail getPlanDetail(Long id) {
        List<PlanDetail> planDetails = planDetailService.getAllPlanDetailByPlanId(id);
        PlanResponseDetail planResponseDetail = new PlanResponseDetail();
        List<CheckinResponse> checkinList = new ArrayList<>();
        planDetails.stream().map(
                planDetail -> {
                    String nameType = planDetail.getTypeEde().getName().trim();
                    if (nameType.equals("Khách sạn") || nameType.equals("Homestay") || nameType.equals("Resort")) {
                        BookingHotel bookingHotel = null;
                        try {
                            bookingHotel = bookingHotelRepository.findById(Long.valueOf(planDetail.getTicketId())).orElseThrow(() -> new RuntimeException("Lỗi lấy thông tin plan"));
                        } catch (Exception e) {
                            log.error("Lỗi lấy thông tin booking hotel: {}", e.getMessage());
                        }
                        planResponseDetail.setHotel_id(bookingHotel.getBookingHotelId());
                        try {
                            planResponseDetail.setHotel_name(hotelService.getByHotelId(bookingHotel.getBookingHotelId()).getName());
                        } catch (Exception e) {
                            log.error("Lỗi set info hotel: {}", e.getMessage());
                            throw new RuntimeException("Lỗi lấy thông tin plan");
                        }
                        List<Map<String, Object>> rooms = new ArrayList<>();
                        for (BookingHotelDetail bookingHotelDetail : bookingHotel.getBookingHotelDetails()) {
                            Map<String, Object> room = new HashMap<>();
                            room.put("room_id", bookingHotelDetail.getRoom().getId());
                            room.put("room_name", bookingHotelDetail.getRoom().getRoomName());
                            room.put("room_price", bookingHotelDetail.getRoom().getPrice());
//                            room.put("room_discount", bookingHotelDetail.);
                            room.put("room_status", bookingHotelDetail.getStatus());
                            rooms.add(room);
                        }
                    }

//                    if (nameType.equals("Checkin")) {
//                        try {
//                            CheckinResponse checkin = checkinService.getCheckin(Long.valueOf(planDetail.getTicketId()));
//                            checkinList.add(checkin);
//                        } catch (Exception e) {
//                            log.error("Lỗi lấy thông tin checkin: {}", e.getMessage());
//                        }
//                    }
//
//                    if (nameType.equals("Xe khách")) {
//                        try {
//                            Ticket ticket = ticketRepository.findById(planDetail.getTicketId()).orElseThrow(() -> new RuntimeException("Lỗi lấy thông tin plan"));
//                            planResponseDetail.setTicket(ticket);
//                        } catch (Exception e) {
//                            log.error("Lỗi lấy thông tin schedule: {}", e.getMessage());
//                        }
//                    }

                    return planDetail;
                }
        ).collect(Collectors.toList());

//        planResponseDetail.setCheckin(checkinList);

        for (PlanDetail planDetail : planDetails) {
            System.out.println(planDetail.getId() + " " + planDetail.getTypeEde().getName() + " " + planDetail.getPlan().getId() + " "
                    + planDetail.getServiceId() + " " + planDetail.getTicketId());
        }
        return planResponseDetail;
    }

    @Override
    public PlanResponse getPlanById(Long id) {
        return null;
    }
}