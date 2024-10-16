package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.response.CarResponse;
import com.be_planfortrips.entity.CarCompany;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CarCompanyMapper implements MapperInterface<CarResponse, CarCompany, CarCompanyDTO> {
    ModelMapper modelMapper;

    @Override
    public CarCompany toEntity(CarCompanyDTO carCompanyDTO) {
        TypeMap<CarCompanyDTO, CarCompany> typeMap = modelMapper.getTypeMap(CarCompanyDTO.class, CarCompany.class);

        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(CarCompanyDTO.class, CarCompany.class);
            typeMap.addMappings(mapper -> mapper.skip(CarCompany::setId));
        }
        CarCompany carCompany = modelMapper.map(carCompanyDTO, CarCompany.class);
        return carCompany;
    }

    @Override
    public CarResponse toResponse(CarCompany carCompany) {
        CarResponse response = modelMapper.map(carCompany,CarResponse.class);
        response.setEnterpriseId(carCompany.getId());
        response.setImageList(carCompany.getImages());
        return response ;
    }

    @Override
    public void updateEntityFromDto(CarCompanyDTO carCompanyDTO, CarCompany carCompany) {

    }
}
