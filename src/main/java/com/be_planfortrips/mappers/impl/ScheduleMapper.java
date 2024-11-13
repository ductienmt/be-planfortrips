package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.entity.Route;
import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.entity.ScheduleSeat;
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

import java.util.ArrayList;
import java.util.List;

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
        Route route = routeRepository.findById(scheduleDto.getRouteId()).orElseThrow(
                () -> new AppException(ErrorType.routeIdNotFound)
        );
        Vehicle vehicleCode = vehicleRepository.findById(scheduleDto.getVehicleCode()).orElseThrow(
                () -> new AppException(ErrorType.vehicleCodeNotFound)
        );

        schedule.setPrice_for_one_seat(scheduleDto.getPriceForOneSeat());
        schedule.setRoute(route);
        schedule.setVehicleCode(vehicleCode);

        return schedule;
    }

    @Override
    public ScheduleResponse toResponse(Schedule schedule) {
        ScheduleResponse scheduleResponse = modelMapper.map(schedule, ScheduleResponse.class);
        Vehicle vehicle = schedule.getVehicleCode();
        scheduleResponse.setRouteId(schedule.getRoute().getId());
        scheduleResponse.setPriceForOneTicket(schedule.getPrice_for_one_seat());

        if (vehicle != null && vehicle.getCarCompany() != null) {
            scheduleResponse.setCode(vehicle.getCode());
            scheduleResponse.setCarCompanyName(vehicle.getCarCompany().getName());
            scheduleResponse.setCarCompanyRating(vehicle.getCarCompany().getRating());
        }

        if (schedule.getRoute() != null) {
            if (schedule.getRoute().getOriginStation() != null) {
                scheduleResponse.setDepartureName(schedule.getRoute().getOriginStation().getName());
            } else {
                scheduleResponse.setDepartureName("Unknown Origin Station");
            }

            if (schedule.getRoute().getDestinationStation() != null) {
                scheduleResponse.setArrivalName(schedule.getRoute().getDestinationStation().getName());
            } else {
                scheduleResponse.setArrivalName("Unknown Destination Station");
            }
        }

        scheduleResponse.setScheduleSeat(schedule.getScheduleSeats());


        return scheduleResponse;
    }


    @Override
    public void updateEntityFromDto(ScheduleDto scheduleDto, Schedule schedule) {

    }
}
