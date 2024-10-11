package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.response.ScheduleResponse;

import java.util.List;

public interface IScheduleService {

    List<ScheduleResponse> getAllSchedule();

    ScheduleResponse getScheduleById(Integer scheduleId);

    ScheduleResponse createSchedule(ScheduleDto scheduleDto);

    ScheduleResponse updateSchedule(ScheduleDto scheduleDto, Integer scheduleId);

    void deleteScheduleById(Integer scheduleId);
}
