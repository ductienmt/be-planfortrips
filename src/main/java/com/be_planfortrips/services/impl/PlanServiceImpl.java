package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.PlanDetailDto;
import com.be_planfortrips.dto.PlanDto;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.dto.response.*;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.interfaces.IPlanService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public Map<String, Object> prepareDataPlan(DataEssentialPlan dataEssentialPlan) {
        Map<String, Object> dataEssential = new HashMap<>();
        dataEssential.put("userData", dataEssentialPlan);

        Map<String, Object> schedulesResponse = this.scheduleService.getAllScheduleByTime(dataEssentialPlan.getStartDate(), dataEssentialPlan.getEndDate(), dataEssentialPlan.getLocation(), dataEssentialPlan.getDestination());
        dataEssential.put("schedules", schedulesResponse);
        // can cai tien: hotel se gan ben xe hon
        Map<String, Object> hotels = this.hotelService.getRoomAvailable(dataEssentialPlan.getStartDate(), dataEssentialPlan.getEndDate(), dataEssentialPlan.getDestination());
        dataEssential.put("hotels", hotels);
        // can cai tien de noi check in gan khach san hon
        List<CheckinResponse> checkinResponses = this.checkinService.getCheckinRandom(5, dataEssentialPlan.getDestination());
        for (CheckinResponse checkinResponse : checkinResponses) {
            checkinResponse.setImages(null);
        }
        dataEssential.put("checkins", checkinResponses);
        return dataEssential;
    }


    @Override
    public List<PlanResponse> getAllPlan() {
        return List.of();
    }

    @Override
    public List<PlanResponse> getAllPlanByUserId() {
        List<Plan> plans = planRepository.findAllByUserId(tokenMapperImpl.getIdUserByToken());

        return plans.stream()
                .sorted(Comparator.comparingLong(Plan::getId).reversed())
                .map(
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
                    planResponse.setDiscount_price(plan.getDiscountPrice());
                    planResponse.setFinal_price(plan.getFinalPrice());
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
                            bookingHotel = bookingHotelRepository.findById(Long.valueOf(planDetail.getTicketId())).orElseThrow(() -> new AppException(ErrorType.notFound, "Lỗi lấy thông tin booking hotel"));
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
                            room.put("status", bookingHotelDetail.getStatus());
                            rooms.add(room);
                        }
                    }
                    if (nameType.equals("Xe khách")) {
                        Ticket ticket = null;
                        try {
                            ticket = ticketRepository.findById(planDetail.getTicketId()).orElseThrow(() -> new AppException(ErrorType.notFound, "Lỗi lấy thông tin ticket"));
                        } catch (Exception e) {
                            log.error("Lỗi lấy thông tin ticket: {}", e.getMessage());
                        }

                        planResponseDetail.setStatus_transport(ticket.getStatus());
                        planResponseDetail.setTransport_price(ticket.getTotalPrice());

                        Schedule schedule = scheduleRepository.findById(ticket.getSchedule().getId()).orElseThrow(() -> new AppException(ErrorType.notFound, "Lỗi lấy thông tin schedule"));

                        Map<String, Object> schedule_transport = new HashMap<>();
                        schedule_transport.put("schedule_id", schedule.getId());
                        schedule_transport.put("schedule_departure_time", schedule.getDepartureTime());
                        schedule_transport.put("schedule_arrival_time", schedule.getArrivalTime());
                        schedule_transport.put("schedule_price_for_one_seat", schedule.getPrice_for_one_seat());
                        schedule_transport.put("schedule_seat", ticket.getSeats());

                        Map<String, Object> routes = new HashMap<>();
                        routes.put("originalLocation", schedule.getRoute().getOriginStation());
                        routes.put("destination", schedule.getRoute().getDestinationStation());

                        Map<String, Object> vehicle = new HashMap<>();
                        vehicle.put("vehicle_name", schedule.getVehicleCode().getCarCompany().getName());
                        vehicle.put("vehicle_type", schedule.getVehicleCode().getTypeVehicle());
                        vehicle.put("vehicle_license_plate", schedule.getVehicleCode().getPlateNumber());
                        vehicle.put("vehicle_driver_name", schedule.getVehicleCode().getDriverName());
                        vehicle.put("vehicle_driver_phone", schedule.getVehicleCode().getDriverPhone());
                    }
                    return planDetail;
                }
        ).collect(Collectors.toList());
        return planResponseDetail;
    }

    @Override
    public Map<String, Object> save(PlanDto planDto) {
        Plan plan = new Plan();
        plan.setPlanName(planDto.getPlanName());
        plan.setStartDate(planDto.getStartDate().toLocalDate());
        plan.setEndDate(planDto.getEndDate().toLocalDate());
        plan.setOriginLocation(planDto.getLocation());
        plan.setDestination(planDto.getDestination());
        plan.setBudget(planDto.getBudget());
        plan.setNumberPeople(planDto.getNumberPeople());
        plan.setTotalPrice(planDto.getTotalPrice());
        plan.setDiscountPrice(planDto.getDiscountPrice());
        plan.setFinalPrice(planDto.getFinalPrice());
        plan.setStatus(StatusPlan.NOT_STARTED);
        plan.setUser(userRepository.findById(tokenMapperImpl.getIdUserByToken()).orElseThrow(() -> new RuntimeException("Lỗi lấy thông tin user")));

        Plan planSave = planRepository.save(plan);
        System.out.println("planSave: " + planSave.getId());

        if (planDto.getPlanDetails() == null || planDto.getPlanDetails().isEmpty()) {
            throw new AppException(ErrorType.internalServerError, "Lỗi lưu kế hoạch");
        }

        if (planSave == null || planSave.getId() == null) {
            throw new AppException(ErrorType.internalServerError, "Lỗi lưu kế hoạch");
        }

        for (PlanDetailDto detailDto : planDto.getPlanDetails()) {
            PlanDetail detail = new PlanDetail();

            Plan planInDetail = planRepository.findById(planSave.getId().longValue()).orElseThrow(() -> new AppException(ErrorType.notFound));
            if (planInDetail != null) {
                detail.setPlan(planInDetail);
            }

            if (detailDto.getHotelId() != null) {
                try {
                    Hotel hotel = hotelRepository.findById(detailDto.getHotelId()).orElseThrow(() -> new AppException(ErrorType.notFound));
                    detail.setTypeEde(hotel.getAccountEnterprise().getTypeEnterpriseDetail());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                detail.setServiceId(detailDto.getHotelId().toString());
                if (detailDto.getTicketId() != null) {
                    bookingHotelRepository.findById(detailDto.getTicketId().longValue()).orElseThrow(() -> new AppException(ErrorType.notFound));
                    detail.setTicketId(detailDto.getTicketId());
                }
            }
            if (detailDto.getCarId() != null) {
                try {
                    Vehicle vehicle = vehicleRepository.findByCode(detailDto.getCarId());
                    if (vehicle == null) {
                        throw new AppException(ErrorType.notFound);
                    }
                    detail.setTypeEde(vehicle.getCarCompany().getEnterprise().getTypeEnterpriseDetail());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                detail.setServiceId(detailDto.getCarId());
                detail.setTicketId(detailDto.getTicketId());
            }
            detail.setTotalPrice(detailDto.getTotalPrice());
            if (detailDto.getStartDate() != null) {
                detail.setStartDate(detailDto.getStartDate());
            } else {
                detail.setStartDate(null);
            }
            if (detailDto.getEndDate() != null) {
                detail.setEndDate(detailDto.getEndDate());
            } else {
                detail.setEndDate(null);
            }

            detail.setStatus(StatusPlan.NOT_STARTED);

            planDetailRepository.save(detail);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("planId", planSave.getId());
        response.put("message", "Lưu kế hoạch thành công");
        return response;
    }

    @Override
    public void checkTime(LocalDate departureDate, LocalDate returnDate) {
        Long userId = tokenMapperImpl.getIdUserByToken();
        List<Plan> overlappingPlans = planRepository.findOverlappingPlans(userId, departureDate, returnDate);

        if (!overlappingPlans.isEmpty()) {
            throw new AppException(ErrorType.exitedPlans);
        }
    }

    @Override
    public PlanResponse getPlanById(Long id) {
        return null;
    }
}