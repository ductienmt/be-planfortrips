package com.be_planfortrips.mappers;

import org.springframework.data.domain.Pageable;

public interface PageMapper {
    Pageable customPage(Integer pageNo, Integer pageSize, String sortBy, String sortType);
}
