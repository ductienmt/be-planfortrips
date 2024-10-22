package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.SeatDTO;
import com.be_planfortrips.dto.VehicleDTO;
import com.be_planfortrips.dto.response.SeatResponse;
import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.entity.CarCompany;
import com.be_planfortrips.entity.Seat;
import com.be_planfortrips.entity.Vehicle;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SeatMapper implements MapperInterface<SeatResponse, Seat, SeatDTO> {
    ModelMapper modelMapper;

    @Override
    public Seat toEntity(SeatDTO seatDTO) {
        TypeMap<SeatDTO, Seat> typeMap = modelMapper.getTypeMap(SeatDTO.class, Seat.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(SeatDTO.class, Seat.class);
        }
        Seat seat = modelMapper.map(seatDTO, Seat.class);
        return seat;
    }

    @Override
    public SeatResponse toResponse(Seat seat) {
        modelMapper.typeMap(Seat.class,SeatResponse.class).addMappings(mapper -> {
//             mapper.map(src -> src.getVehicle(), SeatResponse::setVehicle);
//             mapper.map(src -> src.getStatus(), SeatResponse::setStatusSeat);

        });return modelMapper.map(seat, SeatResponse.class);
    }

    @Override
    public void updateEntityFromDto(SeatDTO seatDTO, Seat seat) {

    }
}
