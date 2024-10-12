package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.response.ScheduleResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IScheduleService {

    List<ScheduleResponse> getAllSchedule();

    ScheduleResponse getScheduleById(Integer scheduleId);

    ScheduleResponse createSchedule(ScheduleDto scheduleDto);

    ScheduleResponse updateSchedule(ScheduleDto scheduleDto, Integer scheduleId);

    void deleteScheduleById(Integer scheduleId);

    Map<String, Object> getAllScheduleByTime(LocalDateTime departureTime, LocalDateTime returnTime);
}
