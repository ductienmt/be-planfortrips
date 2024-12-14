package com.be_planfortrips.services.impl;

import com.be_planfortrips.entity.City;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.CityRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.services.interfaces.ICityService;
import com.be_planfortrips.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CityArea implements ICityService {
    CityRepository cityRepository;
    CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    @Override
    public List<City> getCityByAreaId(String areaId) {
        return cityRepository.findByArea_Id(areaId);
    }

    @Override
    public List<Map<String, Object>> getCitiesByAreaId(String id) {
        List<City> cities = cityRepository.findByArea_Id(id);
        List<Map<String, Object>> citiesArea = new ArrayList<>();
        for (City city : cities) {
            Map<String, Object> cityMap = new HashMap<>();
            cityMap.put("id", city.getId());
            cityMap.put("nameCity", city.getNameCity());
            cityMap.put("description", city.getDescription());
            if (city.getImage() != null) {
                cityMap.put("image", city.getImage().getUrl());
            }
            citiesArea.add(cityMap);
        }
        return citiesArea;
    }

    @Override
    public List<Map<String, Object>> getAllCity() {
        List<City> cities = cityRepository.findAll();
        List<Map<String, Object>> cityList = new ArrayList<>();
        for (City city : cities) {
            Map<String, Object> cityMap = new HashMap<>();
            cityMap.put("id", city.getId());
            cityMap.put("nameCity", city.getNameCity());
            cityMap.put("description", city.getDescription());
            if (city.getImage() != null) {
                cityMap.put("image", city.getImage().getUrl());
            }
            cityList.add(cityMap);
        }
        return cityList;
    }

    @Override
    public List<Map<String, Object>> getFavoriteCity() {
        return cityRepository.getFavoriteCity();
    }

    @Override
    public void uploadImage(MultipartFile file, String city_id) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ảnh hợp lệ");
        }

        City city = cityRepository.findById(city_id).orElseThrow(() -> new AppException(ErrorType.notFound, "Không tìm thấy thành phố"));

        Utils.checkSize(file);

        String imageUrl;
        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, "city_images");
            imageUrl = uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new AppException(ErrorType.internalServerError);
        }

        Image image = new Image();
        image.setUrl(imageUrl);
        imageRepository.saveAndFlush(image);

        city.setImage(image);
        cityRepository.saveAndFlush(city);
    }

    @Override
    public List<String> findCityByName(String name) {
        List<City> cities = cityRepository.searchByNameCityContaining(name);
        List<String> cityNames = new ArrayList<>();
        for(City city: cities){
            cityNames.add(city.getNameCity());
        }
        return cityNames;
    }

    @Override
    public List<Map<String, Object>> getCitiesPopular() {
        return this.cityRepository.getTop3CitiesPopular();
    }
}
