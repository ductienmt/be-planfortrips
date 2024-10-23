package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.dto.response.SeatResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.ScheduleMapper;
import com.be_planfortrips.repositories.ScheduleRepository;
import com.be_planfortrips.repositories.ScheduleSeatRepository;
import com.be_planfortrips.repositories.SeatRepository;
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
    private final SeatRepository seatRepository;
    private final ScheduleSeatRepository scheduleSeatRepository;


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
                scheduleMap.put("seatAvailable", seatResponses);

                scheduleResponseMap.put(schedule.getCarCompanyName(), scheduleMap);
            }
        }

        return scheduleResponseMap;
    }

}
