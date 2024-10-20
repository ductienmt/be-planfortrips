package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.response.CarResponse;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.CarCompany;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.CarCompanyMapper;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.CarCompanyRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.services.interfaces.ICarCompanyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarCompanyService implements ICarCompanyService {
    CarCompanyRepository carCompanyRepository;
    AccountEnterpriseRepository enterpriseRepository;
    CarCompanyMapper carCompanyMapper;
    CloudinaryService cloudinaryService;
    ImageRepository imageRepository;

    @Override
    @Transactional
    public CarResponse createCar(CarCompanyDTO carCompanyDto) throws Exception {
        AccountEnterprise accountEnterprise = enterpriseRepository
                .findById(Long.valueOf(carCompanyDto.getEnterpriseId()))
                .orElseThrow(
                        () -> new Exception("Enterprise not found"));
        CarCompany carCompany = carCompanyMapper.toEntity(carCompanyDto);
        carCompany.setEnterprise(accountEnterprise);
        carCompanyRepository.saveAndFlush(carCompany);
        return carCompanyMapper.toResponse(carCompany);
    }

    @Override
    @Transactional
    public CarResponse updateCar(Integer id, CarCompanyDTO carCompanyDto) throws Exception {
        AccountEnterprise accountEnterprise = enterpriseRepository
                .findById(Long.valueOf(carCompanyDto.getEnterpriseId()))
                .orElseThrow(
                        () -> new Exception("Enter prise not found"));
        CarCompany existingCar = carCompanyRepository.findById(id).orElseThrow(
                () -> new Exception("car company not found"));
        existingCar = carCompanyMapper.toEntity(carCompanyDto);
        existingCar.setId(id);
        existingCar.setEnterprise(accountEnterprise);
        carCompanyRepository.saveAndFlush(existingCar);
        return carCompanyMapper.toResponse(existingCar);
    }

    @Override
    public Page<CarResponse> getCars(PageRequest request) {
        return carCompanyRepository.findAll(request).map(carCompanyMapper::toResponse);
    }

    @Override
    public CarResponse getByCarId(Integer id) throws Exception {
        Optional<CarCompany> carCompany = carCompanyRepository.findById(Math.toIntExact(id));
        if (!carCompany.isPresent())
            new Exception("Not found");
        return carCompanyMapper.toResponse(carCompany.get());
    }

    @Override
    @Transactional
    public void deleteCarById(Integer id) {
        Optional<CarCompany> carCompany = carCompanyRepository.findById(Math.toIntExact(id));
        carCompany.ifPresent(carCompanyRepository::delete);
    }

    @Override
    @Transactional
    public CarResponse uploadImage(Integer id, List<MultipartFile> files) throws Exception {
        CarCompany carCompany = carCompanyRepository.findById(id).orElseThrow(
                () -> new Exception("Not found"));
        List<Image> imageList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }
            // Kiểm tra kích thước file và định dạng
            if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                new Exception("File is too large! Maximum size is 10MB");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                new Exception("File must be an image");
            }
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, "cars");
            String imageUrl = (String) uploadResult.get("secure_url");
            Image image = new Image();
            image.setUrl(imageUrl);
            image = imageRepository.saveAndFlush(image);
            imageList.add(image);
        }
        carCompany.getImages().addAll(imageList);
        carCompanyRepository.saveAndFlush(carCompany);
        return carCompanyMapper.toResponse(carCompany);
    }

    @Override
    @Transactional
    public CarResponse deleteImage(Integer id, List<Integer> imageIds) throws Exception {
        CarCompany carCompany = carCompanyRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorType.notFound));
        List<Image> images = carCompany.getImages();
        List<Image> imagesToDelete = images.stream()
                .filter(image -> imageIds.contains(Integer.valueOf(String.valueOf(image.getId()))))
                .collect(Collectors.toList());
        if (imagesToDelete.isEmpty()) {
            throw new Exception("No images found to delete");
        }

        for (Image image : imagesToDelete) {
            try {
                String publicId = cloudinaryService.getPublicIdFromUrl(image.getUrl());
                cloudinaryService.deleteFile(publicId);
                carCompany.getImages().remove(image);
                imageRepository.delete(image);
            } catch (Exception e) {
                throw new Exception("Error deleting image: " + e.getMessage());
            }
        }
        carCompanyRepository.saveAndFlush(carCompany);
        return carCompanyMapper.toResponse(carCompany);
    }
}
