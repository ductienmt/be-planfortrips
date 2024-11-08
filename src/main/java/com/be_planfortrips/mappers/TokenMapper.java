package com.be_planfortrips.mappers;

public interface TokenMapper {
    Long getIdUserByToken();
    Long getIdAdminByToken();
    Long getIdEnterpriseByToken();
}
