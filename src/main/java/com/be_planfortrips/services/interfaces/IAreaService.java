package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.AreaDto;
import com.be_planfortrips.dto.response.AreaResponse;

import java.util.List;

public interface IAreaService {
    List<AreaResponse> getAll();
    AreaResponse createArea(AreaDto areaDto);
    AreaResponse updateArea(String id, AreaDto areaDto);
    void deleteArea(String id);
    void changeStatusArea(String id, Integer status);
}
