package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.response.CarResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICarCompanyService {
    CarResponse createCar(CarCompanyDTO carCompanyDto) throws Exception;
    CarResponse updateCar(Integer id,CarCompanyDTO carCompanyDto) throws Exception;
    Page<CarResponse> getCars(PageRequest request);
    CarResponse getByCarId(Integer id) throws Exception;
    void deleteCarById(Integer id);
    CarResponse uploadImage(Integer id, List<MultipartFile> files) throws Exception;
    CarResponse deleteImage(Integer id, List<Integer> imageIds) throws Exception;
}
