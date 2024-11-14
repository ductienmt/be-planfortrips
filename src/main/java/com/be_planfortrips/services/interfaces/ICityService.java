package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.entity.City;

import java.util.List;

public interface ICityService {
    List<City> getCityByAreaId(String areaId);
}
