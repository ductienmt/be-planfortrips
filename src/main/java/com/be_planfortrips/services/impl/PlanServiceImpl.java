package com.be_planfortrips.services.impl;

import com.be_planfortrips.controllers.PlanController;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.dto.response.CheckinResponse;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.dto.response.PlanResponse;
import com.be_planfortrips.entity.Plan;
import com.be_planfortrips.entity.PlanDetail;
import com.be_planfortrips.repositories.PlanRepository;
import com.be_planfortrips.services.interfaces.IPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements IPlanService {
    private static final String CHATGPT_API = "sk-OI3g1CMDw2XatjAOR2ITR57Siu0oOXP7Dq5vwLzmVlT3BlbkFJyCZHBKMikCuFGb6f_DfnvlrA3PoeKzz000dJWZ0sEA";

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
    public List<PlanResponse> getAllPlanByUserId(Long userId){
        List<Plan> plans = planRepository.findAllByUserId(userId);

        return plans.stream().map(
                plan -> {
                    PlanResponse planResponse = new PlanResponse();
                    planResponse.setPlan_id(Long.valueOf(plan.getId()));
                    planResponse.setPlan_name(plan.getPlanName());
                    planResponse.setBudget(plan.getBudget());
                    planResponse.setNumberPeople(plan.getNumberPeople());

                    List<PlanDetail> planDetails = planDetailService.getAllPlanDetailByPlanId(Long.valueOf(plan.getId()));

//                    planDetails.stream().map(
//                            planDetail -> {
//                                if (planDetail.getTypeEde().getName().trim().equals("Khách sạn")){
//                                    planResponse.setHotel_id(Long.valueOf(planDetail.getServiceId()));
//                                    HotelResponse hotel =  null;
//                                    try {
//                                        hotel =  hotelService.getByHotelId(Long.valueOf(planDetail.getServiceId()));
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    planResponse.setHotel_name(hotel.getName());
//                                    BookingHotelResponse bookingHotel = null;
//                                    try {
//                                        bookingHotel = bookingHotelService.getBookingHotelById(Long.valueOf(planDetail.getTicketId()));
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    planResponse.setRoom_id(bookingHotel.getRoom().getId());
//                                    planResponse.setRoom_name(bookingHotel.getRoom().getRoomName());
//                                    planResponse.setRoom_price(bookingHotel.getRoom().getPrice());
//                                }
//                                return planDetail;
//                            }
//                    ).collect(Collectors.toList());

//                    for (PlanDetail planDetail : planDetails) {
//                        System.out.println(planDetail.getId() + " " + planDetail.getTypeEde().getName() +" "+ planDetail.getPlan().getId() + " "
//                                + planDetail.getServiceId() + " " + planDetail.getTicketId());
//                    }

                    return planResponse;
                }).collect(Collectors.toList());
    }

    @Override
    public PlanResponse getPlanById(Long id) {

        return null;
    }
}
