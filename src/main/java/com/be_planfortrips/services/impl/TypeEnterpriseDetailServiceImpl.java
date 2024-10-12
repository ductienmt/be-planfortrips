package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TypeEnterpriseDetailDto;
import com.be_planfortrips.dto.response.TypeEnterpriseDetailResponse;
import com.be_planfortrips.entity.TypeEnterpriseDetail;
import com.be_planfortrips.entity.TypeEnterprise;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.TypeEnterpriseDetailMapper;
import com.be_planfortrips.repositories.TypeEnterpriseDetailRepository;
import com.be_planfortrips.repositories.TypeEnterpriseRepository;
import com.be_planfortrips.services.interfaces.ITypeEnterpriseDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TypeEnterpriseDetailServiceImpl implements ITypeEnterpriseDetailService {

    TypeEnterpriseDetailRepository typeEnterpriseDetailRepository;
    TypeEnterpriseRepository typeEnterpriseRepository;
    TypeEnterpriseDetailMapper typeEnterpriseDetailMapper;

    @Override
    public List<TypeEnterpriseDetailResponse> getAllTypeEnterpriseDetails() {
        List<TypeEnterpriseDetail> details = typeEnterpriseDetailRepository.findAll();
        return details.stream()
                .map(typeEnterpriseDetailMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TypeEnterpriseDetailResponse getTypeEnterpriseDetailById(Long id) {
        TypeEnterpriseDetail detail = typeEnterpriseDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        return typeEnterpriseDetailMapper.toResponse(detail);
    }

    @Override
    public TypeEnterpriseDetailResponse createTypeEnterpriseDetail(TypeEnterpriseDetailDto dto) {
        TypeEnterprise typeEnterprise = typeEnterpriseRepository.findById(dto.getTypeEnterpriseId())
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        TypeEnterpriseDetail detail = TypeEnterpriseDetail.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .typeEnterprise(typeEnterprise)
                .build();

        TypeEnterpriseDetail savedDetail = typeEnterpriseDetailRepository.save(detail);
        return typeEnterpriseDetailMapper.toResponse(savedDetail);
    }

    @Override
    public TypeEnterpriseDetailResponse updateTypeEnterpriseDetail(Long id, TypeEnterpriseDetailDto dto) {
        TypeEnterpriseDetail detail = typeEnterpriseDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        TypeEnterprise typeEnterprise = typeEnterpriseRepository.findById(dto.getTypeEnterpriseId())
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        detail.setName(dto.getName());
        detail.setDescription(dto.getDescription());
        detail.setTypeEnterprise(typeEnterprise);

        TypeEnterpriseDetail updatedDetail = typeEnterpriseDetailRepository.save(detail);
        return typeEnterpriseDetailMapper.toResponse(updatedDetail);
    }

    @Override
    public void deleteTypeEnterpriseDetail(Long id) {
        TypeEnterpriseDetail detail = typeEnterpriseDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        typeEnterpriseDetailRepository.delete(detail);
    }
}
