package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.entity.Route;
import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.entity.Vehicle;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.RouteRepository;
import com.be_planfortrips.repositories.SeatRepository;
import com.be_planfortrips.repositories.VehicleRepository;
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
    RouteRepository routeRepository;
    VehicleRepository vehicleRepository;
    SeatRepository seatRepository;
    @Override
    public Schedule toEntity(ScheduleDto scheduleDto) {
        Schedule schedule = modelMapper.map(scheduleDto, Schedule.class);
        String routeId = scheduleDto.getRouteId();
        String vehicleCode = scheduleDto.getVehicleCode();

        if (!routeRepository.existsById(routeId)) {
                throw new AppException(ErrorType.routeIdNotFound, routeId);
        }
        if (!vehicleRepository.existsById(vehicleCode)) {
            throw new AppException(ErrorType.vehicleCodeNotFound, vehicleCode);
        }
        schedule.setRoute(Route.builder().id(routeId).build());
        schedule.setVehicleCode(Vehicle.builder().code(vehicleCode).build());
        return schedule;
    }

    @Override
    public ScheduleResponse toResponse(Schedule schedule) {
        ScheduleResponse scheduleResponse = modelMapper.map(schedule, ScheduleResponse.class);
        Vehicle vehicle = schedule.getVehicleCode();
        scheduleResponse.setRouteId(schedule.getRoute().getId());
        // Vehicle
        scheduleResponse.setCode(vehicle.getCode());
        scheduleResponse.setCarCompanyName(vehicle.getCarCompany().getName());
        scheduleResponse.setCarCompanyRating(vehicle.getCarCompany().getRating());

        Long countSeatsEmpty = seatRepository.countEmptySeatsByVehicleCode(vehicle.getCode());
        scheduleResponse.setCountSeatsEmpty(countSeatsEmpty);

        return scheduleResponse;
    }

    @Override
    public void updateEntityFromDto(ScheduleDto scheduleDto, Schedule schedule) {

    }
}
