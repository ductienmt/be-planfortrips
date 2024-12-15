package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.entity.City;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ICityService {
    List<City> getCityByAreaId(String areaId);
    List<Map<String, Object>> getCitiesByAreaId(String id);
    List<Map<String, Object>> getAllCity();
    List<Map<String, Object>> getFavoriteCity();
    void uploadImage(MultipartFile file, String city_id);
    List<String> findCityByName(String name);
    List<Map<String, Object>> getCitiesPopular();
}
