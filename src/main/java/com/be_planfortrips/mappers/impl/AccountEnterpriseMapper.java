package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountEnterpriseMapper implements
        MapperInterface<AccountEnterpriseResponse, AccountEnterprise, AccountEnterpriseDto> {

    ModelMapper modelMapper;
    @Override
    public AccountEnterprise toEntity(AccountEnterpriseDto accountEnterpriseDto) {
        AccountEnterprise enterprise = modelMapper.map(accountEnterpriseDto, AccountEnterprise.class);
        return enterprise;
    }

    @Override
    public AccountEnterpriseResponse toResponse(AccountEnterprise accountEnterprise) {
        AccountEnterpriseResponse accountEnterpriseResponse =
                modelMapper.map(accountEnterprise, AccountEnterpriseResponse.class);
        return accountEnterpriseResponse;
    }

    @Override
    public void updateEntityFromDto(AccountEnterpriseDto accountEnterpriseDto, AccountEnterprise accountEnterprise) {

    }
}
