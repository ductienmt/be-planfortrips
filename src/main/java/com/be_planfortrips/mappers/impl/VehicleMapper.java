package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.VehicleDTO;
import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.entity.CarCompany;
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
public class VehicleMapper implements MapperInterface<VehicleResponse, Vehicle, VehicleDTO> {
    ModelMapper modelMapper;
    @Override
    public Vehicle toEntity(VehicleDTO vehicleDTO) {
        TypeMap<VehicleDTO, Vehicle> typeMap = modelMapper.getTypeMap(VehicleDTO.class, Vehicle.class);

        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(VehicleDTO.class, Vehicle.class);
        }
        Vehicle vehicle = modelMapper.map(vehicleDTO, Vehicle.class);
        return vehicle;
    }

    @Override
    public VehicleResponse toResponse(Vehicle vehicle) {
        modelMapper.typeMap(Vehicle.class,VehicleResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getTypeVehicle(), VehicleResponse::setTypeVehicle);
        });
        return modelMapper.map(vehicle,VehicleResponse.class);
    }

    @Override
    public void updateEntityFromDto(VehicleDTO vehicleDTO, Vehicle vehicle) {

    }
}
