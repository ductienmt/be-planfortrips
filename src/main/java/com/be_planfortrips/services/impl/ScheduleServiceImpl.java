package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.ScheduleMapper;
import com.be_planfortrips.repositories.ScheduleRepository;
import com.be_planfortrips.services.interfaces.IScheduleService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class ScheduleServiceImpl implements IScheduleService {

    ScheduleRepository scheduleRepository;
    ScheduleMapper scheduleMapper;
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
        return scheduleMapper.toResponse(
                scheduleRepository.save(schedule));
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

//    @Override
//    public List<ScheduleResponse> getAllScheduleByTime(LocalDateTime departureTime, LocalDateTime arrivalTime) {
//        return this.scheduleRepository.findSchedulesAfterSpecificTime(
//                departureTime.toLocalDate(),
//                departureTime.toLocalTime(),
//                arrivalTime.toLocalDate(),
//                arrivalTime.toLocalTime()
//        ).stream().map(scheduleMapper::toResponse).collect(Collectors.toList());
//    }
}
