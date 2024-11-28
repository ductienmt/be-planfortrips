package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.request.DataSchedule;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.dto.response.SeatResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.ScheduleMapper;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.interfaces.IScheduleService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class ScheduleServiceImpl implements IScheduleService {


    ScheduleRepository scheduleRepository;
    ScheduleMapper scheduleMapper;
    SeatService seatService;

    SeatRepository seatRepository;
    VehicleRepository vehicleRepository;
    RouteRepository routeRepository;
    ScheduleSeatRepository scheduleSeatRepository;

    @Override
    public List<ScheduleResponse> getAllSchedule() {
        return scheduleRepository.findAll().
                stream().map(scheduleMapper::toResponse).toList();
    }

    @Override
    public ScheduleResponse getScheduleById(Integer scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );
        return scheduleMapper.toResponse(schedule);
    }

    @Override
    public ScheduleResponse createSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = scheduleMapper.toEntity(scheduleDto);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        List<Seat> seats = seatRepository.findByVehicleCode(scheduleDto.getVehicleCode());

        if (seats.isEmpty()) {
            throw new AppException(ErrorType.notFound);
        }

        for (Seat seat : seats) {
            ScheduleSeat scheduleSeat = new ScheduleSeat();
            scheduleSeat.setSeat(seat);
            scheduleSeat.setSeatNumber(seat.getSeatNumber());
            scheduleSeat.setSchedule(savedSchedule);
            scheduleSeat.setStatus(StatusSeat.Empty);
            scheduleSeatRepository.save(scheduleSeat);
        }
        return scheduleMapper.toResponse(savedSchedule);
    }

    @Override
    public ScheduleResponse updateSchedule(ScheduleDto scheduleDto, Integer scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );
        schedule.setId(scheduleId);
        List<Seat> seats = seatRepository.findByVehicleCode(scheduleDto.getVehicleCode());

        for (Seat seat : seats) {
            ScheduleSeat scheduleSeat = new ScheduleSeat();
            scheduleSeat.setSeat(seat);
            scheduleSeat.setSeatNumber(seat.getSeatNumber());
            scheduleSeat.setSchedule(schedule);
            scheduleSeat.setStatus(StatusSeat.Empty);
            scheduleSeatRepository.save(scheduleSeat);
        }

        Schedule scheduleNew = scheduleMapper.toEntity(scheduleDto);
        scheduleNew.setId(scheduleId);

        return scheduleMapper.toResponse(
                scheduleRepository.saveAndFlush(scheduleNew));
    }

    @Override
    public void deleteScheduleById(Integer scheduleId) {
        scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );
        scheduleRepository.deleteById(scheduleId);
    }

    @Override
    public Map<String, Object> getRouteByScheduleId(Long scheduleId) {
        Map<String, Object> station = scheduleRepository.findStationsByScheduleId(scheduleId);
        return station;
    }

    @Override
    public Map<String, Object> getAllScheduleByTime(LocalDateTime departureTime, LocalDateTime returnTime, String originalLocation, String destination) {
        System.out.println("Departure Time: " + departureTime);
        Map<String, Object> departureResponse = fetchSchedules(departureTime, "departure", originalLocation, destination);
        Map<String, Object> returnResponse = fetchSchedules(returnTime, "return", originalLocation, destination);
        System.out.println("Departure Response: " + departureResponse);
        System.out.println("Return Response: " + returnResponse);
        if(departureResponse.isEmpty()) {
            departureTime = departureTime.plusDays(1).withHour(0).withMinute(0).withSecond(0);
            System.out.println("Departure Time: " + departureTime);
            departureResponse = fetchSchedules(departureTime, "departure", originalLocation, destination);
            System.out.println("Departure Response 2: " + departureResponse);
        }

        if(returnResponse.isEmpty()) {
            returnTime = returnTime.plusDays(1).withHour(0).withMinute(0).withSecond(0);
            System.out.println("Return Time: " + returnTime);
            returnResponse = fetchSchedules(returnTime, "return", originalLocation, destination);
            System.out.println("Return Response 2: " + returnResponse);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("departure", departureResponse);
        response.put("return", returnResponse);

        if (response.isEmpty()) {
            throw new AppException(ErrorType.notFound);
        }

        return response;
    }


    @Override
    public List<ScheduleResponse> getSchedules(DataSchedule dataSchedule) {
        if (dataSchedule.getEndDate() == null) {
            dataSchedule.setEndDate(dataSchedule.getStartDate());
        }
        List<Schedule> schedules = this.scheduleRepository.findSchedule(
                dataSchedule.getOriginalLocation(),
                dataSchedule.getDestination(),
                dataSchedule.getStartDate()
        );
        List<Schedule> reverseSchedules = this.scheduleRepository.findSchedule(
                dataSchedule.getDestination(),
                dataSchedule.getOriginalLocation(),
                dataSchedule.getEndDate()
        );
        schedules.addAll(reverseSchedules);
        return schedules.stream().map(scheduleMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getScheduleByVehicleCodeAndRouteId
            (String vehicleCode, String routeId) {
        // Check Vehicle
        Vehicle vehicle = vehicleRepository.findById(vehicleCode).orElseThrow(
                () -> new AppException(ErrorType.VehicleCodeNotFound)
        );
        Route route = routeRepository.findById(routeId).orElseThrow(
                () -> new AppException(ErrorType.RouteIdNotFound)
        );
        List<Schedule> schedules =
                scheduleRepository.getSchedulesByRouteAndVehicleCode(route, vehicle);
        return schedules.stream().map(scheduleMapper::toResponse).toList();
    }

    private Map<String, Object> fetchSchedules(LocalDateTime time, String type, String originalLocation, String destination) {
        List<ScheduleResponse> schedules = new ArrayList<>();
        if (type.equals("departure")) {
            schedules = this.scheduleRepository.findSchedulesAfterSpecificTime(
                    time.toLocalDate(),
                    time.toLocalTime(),
                    originalLocation,
                    destination
            ).stream().map(scheduleMapper::toResponse).toList();
        } else {
            schedules = this.scheduleRepository.findSchedulesAfterSpecificTime(
                    time.toLocalDate(),
                    time.toLocalTime(),
                    destination,
                    originalLocation
            ).stream().map(scheduleMapper::toResponse).toList();
        }

        Map<String, Object> scheduleResponseMap = new HashMap<>();
        for (ScheduleResponse scheduleResponse : schedules) {
            ScheduleResponse schedule = this.getScheduleById(scheduleResponse.getId());
            List<SeatResponse> seatResponses = seatService.getEmptySeatsByScheduleId(scheduleResponse.getId());

            if (!seatResponses.isEmpty()) {
                Map<String, Object> scheduleMap = new HashMap<>();
                scheduleMap.put("scheduleId", schedule.getId());
                scheduleMap.put("vehicleCode", schedule.getCode());
                scheduleMap.put("carName", schedule.getCarCompanyName());
                scheduleMap.put("departureTime", schedule.getDepartureTime());
                scheduleMap.put("arrivalTime", schedule.getArrivalTime());
                scheduleMap.put("routeId", schedule.getRouteId());
                scheduleMap.put("priceForOneSeat", schedule.getPriceForOneTicket());
                scheduleMap.put("seatAvailable", seatResponses);

                scheduleResponseMap.put(schedule.getCarCompanyName(), scheduleMap);
            }
        }

        return scheduleResponseMap;
    }

}
