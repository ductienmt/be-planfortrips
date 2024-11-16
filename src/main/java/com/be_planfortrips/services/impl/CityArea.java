package com.be_planfortrips.services.impl;

import com.be_planfortrips.entity.City;
import com.be_planfortrips.repositories.CityRepository;
import com.be_planfortrips.services.interfaces.ICityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CityArea implements ICityService {
    CityRepository cityRepository;
    @Override
    public List<City> getCityByAreaId(String areaId) {
        return cityRepository.findByArea_Id(areaId);
    }
}
