package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
public class ScheduleMapper implements MapperInterface<ScheduleResponse, Schedule, ScheduleDto> {

    ModelMapper modelMapper;
    @Override
    public Schedule toEntity(ScheduleDto scheduleDto) {
        Schedule schedule = modelMapper.map(scheduleDto, Schedule.class);
        return schedule;
    }

    @Override
    public ScheduleResponse toResponse(Schedule schedule) {
        ScheduleResponse scheduleResponse = modelMapper.map(schedule, ScheduleResponse.class);
        return scheduleResponse;
    }

    @Override
    public void updateEntityFromDto(ScheduleDto scheduleDto, Schedule schedule) {

    }
}
