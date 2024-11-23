package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.SeatDTO;
import com.be_planfortrips.dto.response.SeatResponse;
import com.be_planfortrips.entity.ScheduleSeat;
import com.be_planfortrips.entity.Seat;
import com.be_planfortrips.entity.Vehicle;
import com.be_planfortrips.mappers.impl.SeatMapper;
import com.be_planfortrips.repositories.ScheduleSeatRepository;
import com.be_planfortrips.repositories.SeatRepository;
import com.be_planfortrips.repositories.VehicleRepository;
import com.be_planfortrips.services.interfaces.ISeatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SeatService implements ISeatService {
    SeatRepository seatRepository;
    SeatMapper seatMapper;
    VehicleRepository vehicleRepository;
    ScheduleSeatRepository scheduleSeatRepository;


    @Override
    @Transactional
    public SeatResponse createSeat(SeatDTO seatDTO) throws Exception {
        Vehicle vehicle = vehicleRepository.findByCode(seatDTO.getVehicleCode());
        if(vehicle==null){
            throw new Exception("Vehicle not found");
        }
        Seat seat = seatMapper.toEntity(seatDTO);
        seat.setVehicle(vehicle);
        seatRepository.saveAndFlush(seat);
        return seatMapper.toResponse(seat);
    }

    @Override
    @Transactional
    public SeatResponse updateSeat(Integer id, SeatDTO seatDTO) throws Exception {
        Vehicle vehicle = vehicleRepository.findByCode(seatDTO.getVehicleCode());
        if(vehicle==null){
            throw new Exception("Vehicle not found");
        }
        Seat existingSeat = seatRepository.findById(id)
                .orElseThrow(
                        ()-> new Exception("Seat not found")
                );
        existingSeat = seatMapper.toEntity(seatDTO);
        existingSeat.setVehicle(vehicle);
        existingSeat.setId(id);
        seatRepository.saveAndFlush(existingSeat);
        return seatMapper.toResponse(existingSeat);
    }

    @Override
    public Page<SeatResponse> getSeats(PageRequest request) {
        return seatRepository.findAll(request).map(seatMapper::toResponse);
    }

    @Override
    public SeatResponse getBySeatId(Integer id) throws Exception {
        Seat existingSeat = seatRepository.findById(id)
                .orElseThrow(
                        ()-> new Exception("Seat not found")
                );
        return seatMapper.toResponse(existingSeat);
    }

    @Override
    @Transactional
    public void deleteSeatById(Integer id) {
        Optional<Seat> seat = seatRepository.findById(id);
        seat.ifPresent(seatRepository::delete);
    }

    @Override
    public List<SeatResponse> getEmptySeatsByScheduleId(Integer scheduleId) {
        List<ScheduleSeat> emptySeats = scheduleSeatRepository.findEmptySeatsByScheduleId(scheduleId);
        return emptySeats.stream().map(emptySeat -> {
            SeatResponse seatResponse = new SeatResponse();
            seatResponse.setId(emptySeat.getSeat().getId());
            seatResponse.setSeatNumber(emptySeat.getSeatNumber());
            seatResponse.setStatus(emptySeat.getStatus().toString());
            return seatResponse;
        }).collect(Collectors.toList());
    }


}
