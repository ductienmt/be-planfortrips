package com.be_planfortrips.services.impl;

import com.be_planfortrips.entity.TypeEnterprise;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.TypeEnterpriseRepository;
import com.be_planfortrips.services.interfaces.ITypeEnterpriseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TypeEnterpriseServiceImpl implements ITypeEnterpriseService {

    private final TypeEnterpriseRepository typeEnterpriseRepository;

    @Override
    public List<TypeEnterprise> getAllTypeEnterprises() {
        return typeEnterpriseRepository.findAll();
    }

    @Override
    public TypeEnterprise getTypeEnterpriseById(Long id) {
        return typeEnterpriseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));
    }

    @Override
    public TypeEnterprise createTypeEnterprise(TypeEnterprise typeEnterprise) {
        boolean nameType = typeEnterpriseRepository.existsByNameType(typeEnterprise.getNameType());
        if (nameType) {
            throw new AppException(ErrorType.nameTypeExisted);
        }
        return typeEnterpriseRepository.save(typeEnterprise);
    }

    @Override
    public TypeEnterprise updateTypeEnterprise(Long id, TypeEnterprise typeEnterprise) {
        TypeEnterprise existingTypeEnterprise = typeEnterpriseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        existingTypeEnterprise.setNameType(typeEnterprise.getNameType());
        existingTypeEnterprise.setDescription(typeEnterprise.getDescription());
        return typeEnterpriseRepository.save(existingTypeEnterprise);
    }

    @Override
    public void deleteTypeEnterpriseById(Long id) {
        TypeEnterprise typeEnterprise = typeEnterpriseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        Boolean hasEtp = typeEnterpriseRepository.checkTypeEnterpriseHasEnterprise(id);
        if (hasEtp) {
            throw new AppException(ErrorType.hasEtp);
        }
        typeEnterpriseRepository.delete(typeEnterprise);
    }
}
