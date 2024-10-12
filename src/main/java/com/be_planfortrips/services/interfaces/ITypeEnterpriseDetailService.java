package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.TypeEnterpriseDetailDto;
import com.be_planfortrips.dto.response.TypeEnterpriseDetailResponse;

import java.util.List;

public interface ITypeEnterpriseDetailService {

    List<TypeEnterpriseDetailResponse> getAllTypeEnterpriseDetails();

    TypeEnterpriseDetailResponse getTypeEnterpriseDetailById(Long id);

    TypeEnterpriseDetailResponse createTypeEnterpriseDetail(TypeEnterpriseDetailDto dto);

    TypeEnterpriseDetailResponse updateTypeEnterpriseDetail(Long id, TypeEnterpriseDetailDto dto);

    void deleteTypeEnterpriseDetail(Long id);
}
