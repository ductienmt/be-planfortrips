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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Map<String, Object> getAllScheduleByTime(LocalDateTime departureTime, LocalDateTime returnTime) {
        List<ScheduleResponse> departure = this.scheduleRepository.findSchedulesAfterSpecificTime(
                departureTime.toLocalDate(),
                departureTime.toLocalTime()
        ).stream().map(scheduleMapper::toResponse).collect(Collectors.toList());
        List<ScheduleResponse> returnSchedules = this.scheduleRepository.findSchedulesAfterSpecificTime(
                returnTime.toLocalDate(),
                returnTime.toLocalTime()
        ).stream().map(scheduleMapper::toResponse).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("departure", departure);
        response.put("return", returnSchedules);
        if (response.isEmpty()) {
            throw new AppException(ErrorType.notFound); // Ném lỗi nếu không tìm thấy chuyến đi
        }
        return response;
    }
}
