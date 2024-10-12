package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.TypeEnterpriseDetailDto;
import com.be_planfortrips.dto.response.TypeEnterpriseDetailResponse;
import com.be_planfortrips.entity.TypeEnterpriseDetail;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
public class TypeEnterpriseDetailMapper implements MapperInterface<TypeEnterpriseDetailResponse, TypeEnterpriseDetail, TypeEnterpriseDetailDto> {

    ModelMapper modelMapper;

    @Override
    public TypeEnterpriseDetail toEntity(TypeEnterpriseDetailDto typeEnterpriseDetailDto) {
        return modelMapper.map(typeEnterpriseDetailDto, TypeEnterpriseDetail.class);
    }

    @Override
    public TypeEnterpriseDetailResponse toResponse(TypeEnterpriseDetail typeEnterpriseDetail) {
        return modelMapper.map(typeEnterpriseDetail, TypeEnterpriseDetailResponse.class);
    }

    @Override
    public void updateEntityFromDto(TypeEnterpriseDetailDto typeEnterpriseDetailDto, TypeEnterpriseDetail typeEnterpriseDetail) {
        typeEnterpriseDetail.setName(typeEnterpriseDetailDto.getName());
        typeEnterpriseDetail.setDescription(typeEnterpriseDetailDto.getDescription());
    }
}
