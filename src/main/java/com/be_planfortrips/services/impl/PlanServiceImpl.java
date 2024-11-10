package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.PlanDetailDto;
import com.be_planfortrips.dto.PlanDto;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.dto.response.*;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.interfaces.IPlanService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
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
    private TypeEnterpriseDetailRepository typeEnterpriseDetailRepository;

    @Autowired
    private PlanDetailRepository planDetailRepository;
    @Autowired
    private TokenMapperImpl tokenMapperImpl;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> prepareDataPlan(DataEssentialPlan dataEssentialPlan) {
        Map<String, Object> dataEssential = new HashMap<>();
        dataEssential.put("userData", dataEssentialPlan);
        List<CheckinResponse> checkinResponses = this.checkinService.getCheckinRandom(5, dataEssentialPlan.getDestination());
        for (CheckinResponse checkinResponse : checkinResponses) {
            checkinResponse.setImages(null);
        }
        dataEssential.put("checkins", checkinResponses);
        Map<String, Object> schedulesResponse = this.scheduleService.getAllScheduleByTime(dataEssentialPlan.getStartDate(), dataEssentialPlan.getEndDate(), dataEssentialPlan.getLocation(), dataEssentialPlan.getDestination());
        dataEssential.put("schedules", schedulesResponse);
        Map<String, Object> hotels = this.hotelService.getRoomAvailable(dataEssentialPlan.getStartDate(), dataEssentialPlan.getEndDate(), dataEssentialPlan.getDestination());
        dataEssential.put("hotels", hotels);
        return dataEssential;
    }

    @Override
    public List<PlanResponse> getAllPlan() {
        return List.of();
    }

    @Override
    public List<PlanResponse> getAllPlanByUserId() {
        List<Plan> plans = planRepository.findAllByUserId(tokenMapperImpl.getIdUserByToken());

        return plans.stream().map(
                plan -> {
                    PlanResponse planResponse = new PlanResponse();
                    planResponse.setPlan_id(Long.valueOf(plan.getId()));
                    planResponse.setPlan_name(plan.getPlanName());
                    planResponse.setBudget(plan.getBudget());
                    planResponse.setNumberPeople(plan.getNumberPeople());
                    planResponse.setStatus(plan.getStatus());
                    planResponse.setStart_date(plan.getStartDate());
                    planResponse.setEnd_date(plan.getEndDate());
                    planResponse.setDestination(plan.getDestination());
                    planResponse.setOrigin_location(plan.getOriginLocation());
                    planResponse.setTotal_price(plan.getTotalPrice());
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
                            room.put("room_status", bookingHotelDetail.getStatus());
                            rooms.add(room);
                        }
                    }
                    return planDetail;
                }
        ).collect(Collectors.toList());

        for (PlanDetail planDetail : planDetails) {
            System.out.println(planDetail.getId() + " " + planDetail.getTypeEde().getName() + " " + planDetail.getPlan().getId() + " "
                    + planDetail.getServiceId() + " " + planDetail.getTicketId());
        }
        return planResponseDetail;
    }

    @Override
    public void save(PlanDto planDto) {
        Plan plan = new Plan();
        plan.setPlanName(planDto.getPlanName());
        plan.setStartDate(planDto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        plan.setEndDate(planDto.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        plan.setOriginLocation(planDto.getLocation());
        plan.setDestination(planDto.getDestination());
        plan.setBudget(planDto.getBudget());
        plan.setNumberPeople(planDto.getNumberPeople());
        plan.setTotalPrice(planDto.getTotalPrice());
        plan.setStatus(StatusPlan.NOT_STARTED);
        plan.setUser(userRepository.findById(tokenMapperImpl.getIdUserByToken()).orElseThrow(() -> new RuntimeException("Lỗi lấy thông tin user")));

        plan = planRepository.save(plan);

        for (PlanDetailDto detailDto : planDto.getPlanDetails()) {
            PlanDetail detail = new PlanDetail();
            detail.setPlan(plan);
            detail.setServiceId(detailDto.getServiceId());
            detail.setTypeEde(typeEnterpriseDetailRepository.findById(Long.valueOf(detailDto.getTypeEdeId())).orElseThrow(() -> new RuntimeException("Lỗi lấy thông tin type enterprise")));
            detail.setTotalPrice(detailDto.getTotalPrice());
            detail.setStartDate(detailDto.getStartDate());
            detail.setEndDate(detailDto.getEndDate());
            detail.setTicketId(detailDto.getTicketId());
            detail.setStatus(detailDto.getStatus());

            planDetailRepository.save(detail);
        }
    }

    @Override
    public PlanResponse getPlanById(Long id) {
        return null;
    }
}