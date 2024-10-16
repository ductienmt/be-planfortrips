package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.TypeEnterpriseDetailDto;
import com.be_planfortrips.dto.response.TypeEnterpriseDetailResponse;
import com.be_planfortrips.entity.TypeEnterprise;
import com.be_planfortrips.entity.TypeEnterpriseDetail;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.TypeEnterpriseRepository;
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
    TypeEnterpriseRepository typeEnterpriseRepository;

    @Override
    public TypeEnterpriseDetail toEntity(TypeEnterpriseDetailDto typeEnterpriseDetailDto) {
        TypeEnterpriseDetail enterpriseDetail = modelMapper.map(typeEnterpriseDetailDto, TypeEnterpriseDetail.class);
        Long typeEnterpriseId = typeEnterpriseDetailDto.getTypeEnterpriseId();
        if (!typeEnterpriseRepository.existsById(typeEnterpriseId)) {
            throw new AppException(ErrorType.typeEnterpriseIdNotFound, typeEnterpriseId);
        }
        enterpriseDetail.setTypeEnterprise(TypeEnterprise.builder().id(typeEnterpriseId).build());
        return enterpriseDetail;
    }

    @Override
    public TypeEnterpriseDetailResponse toResponse(TypeEnterpriseDetail typeEnterpriseDetail) {
        TypeEnterpriseDetailResponse response =
                modelMapper.map(typeEnterpriseDetail, TypeEnterpriseDetailResponse.class);
        response.setTypeEnterpriseId(typeEnterpriseDetail.getTypeEnterprise().getId());
        return response;
    }

    @Override
    public void updateEntityFromDto(TypeEnterpriseDetailDto typeEnterpriseDetailDto, TypeEnterpriseDetail typeEnterpriseDetail) {
        typeEnterpriseDetail.setName(typeEnterpriseDetailDto.getName());
        typeEnterpriseDetail.setDescription(typeEnterpriseDetailDto.getDescription());
    }
}
