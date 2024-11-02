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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    public Map<String, Object> getAllScheduleByTime(LocalDateTime departureTime, LocalDateTime returnTime) {
        Map<String, Object> departureResponse = fetchSchedules(departureTime, "departure");
        Map<String, Object> returnResponse = fetchSchedules(returnTime, "return");

        Map<String, Object> response = new HashMap<>();
        response.put("departure", departureResponse);
        response.put("return", returnResponse);

        if (response.isEmpty() || (departureResponse.isEmpty() && returnResponse.isEmpty())) {
            throw new AppException(ErrorType.notFound);
        }

        return response;
    }

    @Override
    public List<Schedule> getSchedules(DataSchedule dataSchedule) {
        if (dataSchedule.getEndDate() == null) {
            dataSchedule.setEndDate(dataSchedule.getStartDate());
        }
        List<Schedule> schedules = this.scheduleRepository.findSchedule(
                dataSchedule.getOriginalLocation(),
                dataSchedule.getDestination(),
                dataSchedule.getStartDate(),
                dataSchedule.getEndDate()
        );
        return schedules;
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

    private Map<String, Object> fetchSchedules(LocalDateTime time, String type) {
        List<ScheduleResponse> schedules = this.scheduleRepository.findSchedulesAfterSpecificTime(
                time.toLocalDate(),
                time.toLocalTime()
        ).stream().map(scheduleMapper::toResponse).toList();

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
