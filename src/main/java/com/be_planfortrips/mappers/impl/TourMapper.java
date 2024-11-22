package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.VehicleDTO;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.entity.CarCompany;
import com.be_planfortrips.entity.Tour;
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
public class TourMapper implements MapperInterface<TourResponse, Tour, TourDTO> {
    ModelMapper modelMapper;

    @Override
    public Tour toEntity(TourDTO TourDTO) {
        TypeMap<TourDTO, Tour> typeMap = modelMapper.getTypeMap(TourDTO.class, Tour.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(TourDTO.class, Tour.class);
        }
        Tour Tour = modelMapper.map(TourDTO, Tour.class);
        Tour.setActive(TourDTO.getIsActive());
        return Tour;
    }

    @Override
    public TourResponse toResponse(Tour Tour) {
        modelMapper.typeMap(Tour.class,TourResponse.class).addMappings(mapper -> {
//             mapper.map(src -> src.getVehicle(), TourResponse::setVehicle);
//             mapper.map(src -> src.getStatus(), TourResponse::setStatusTour);

        });return modelMapper.map(Tour, TourResponse.class);
    }

    @Override
    public void updateEntityFromDto(TourDTO TourDTO, Tour Tour) {

    }
}
