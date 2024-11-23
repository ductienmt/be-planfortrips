package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.mappers.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageMapperImpl implements PageMapper {
    @Override
    public Pageable customPage(Integer pageNo, Integer pageSize, String sortBy, String sortType) {
        Sort sortable;
        if (sortType.equals("asc")) {
            sortable = Sort.by(sortBy).ascending();
        } else {
            sortable = Sort.by(sortBy).descending();
        }
        return PageRequest.of(pageNo, pageSize, sortable);
    }
}
