package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.CheckinDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.CheckinResponse;
import com.be_planfortrips.entity.Checkin;
import com.be_planfortrips.entity.CheckinImage;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.CheckinMapper;
import com.be_planfortrips.repositories.CheckinImageRepository;
import com.be_planfortrips.repositories.CheckinRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.services.interfaces.ICheckinService;
import com.be_planfortrips.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckinServiceImpl implements ICheckinService {
    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CheckinImageRepository checkinImageRepository;

    @Autowired
    private Utils utils;

    @Override
    public CheckinResponse createCheckin(CheckinDto checkinDto) {
        Checkin checkin = this.checkinRepository.save(this.checkinMapper.toEntity(checkinDto));
        return this.checkinMapper.toResponse(checkin);
    }

    @Override
    public CheckinResponse updateCheckin(Long id, CheckinDto checkinDto) {
        Checkin checkin = this.checkinRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy điểm checkin"));
        this.checkinMapper.updateEntityFromDto(checkinDto, checkin);
        this.checkinRepository.saveAndFlush(checkin);
        return this.checkinMapper.toResponse(checkin);

    }

    @Override
    public void deleteCheckin(Long id) {
        Checkin checkin = this.checkinRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy điểm checkin"));
        if (checkin != null) {
            this.checkinRepository.delete(checkin);
        }
    }

    @Override
    public CheckinResponse getCheckin(Long id) {
        return this.checkinMapper.toResponse(this.checkinRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy điểm checkin")));
    }

    @Override
    public Map<String, Object> getAllCheckin(Integer page) {
        int pageSize = 10;
        if (page < 1) {
            throw new IllegalArgumentException("Số trang phải lớn hơn hoặc bằng 1");
        }
        Page<Checkin> checkinPage = this.checkinRepository.findAll(PageRequest.of(page - 1, pageSize));
        List<CheckinResponse> checkinResponses = checkinPage.getContent()
                .stream().map(checkinMapper::toResponse).collect(Collectors.toList());
        int totalPages = checkinPage.getTotalPages();
        long totalElements = checkinPage.getTotalElements();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("totalElements", totalElements);
        responseMap.put("totalPages", totalPages);
        responseMap.put("checkinResponses", checkinResponses);
        if (responseMap.isEmpty()) {
            throw new AppException(ErrorType.notFound);
        }
        return responseMap;
    }

    @Override
    public Map<String, Object> getCheckinByCityName(String cityName, Integer page) {
        int pageSize = 10;
        if (page < 1) {
            throw new IllegalArgumentException("Số trang phải lớn hơn hoặc bằng 1");
        }
        Page<Checkin> checkinPage = this.checkinRepository.findByCityName(cityName, PageRequest.of(page - 1, pageSize));
        List<CheckinResponse> checkinResponses = checkinPage.getContent()
                .stream().map(checkinMapper::toResponse).collect(Collectors.toList());
        int totalPages = checkinPage.getTotalPages();
        long totalElements = checkinPage.getTotalElements();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("totalElements", totalElements);
        responseMap.put("totalPages", totalPages);
        responseMap.put("checkinResponses", checkinResponses);
        return responseMap;
    }

    @Override
    public Map<String, Object> getCheckinByName(String name, Integer page) {
        int pageSize = 10;
        if (page < 1) {
            throw new IllegalArgumentException("Số trang phải lớn hơn hoặc bằng 1");
        }
        Page<Checkin> checkinPage = this.checkinRepository.findByName(name, PageRequest.of(page - 1, pageSize));
        List<CheckinResponse> checkinResponses = checkinPage.getContent()
                .stream().map(checkinMapper::toResponse).collect(Collectors.toList());
        int totalPages = checkinPage.getTotalPages();
        long totalElements = checkinPage.getTotalElements();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("totalElements", totalElements);
        responseMap.put("totalPages", totalPages);
        responseMap.put("checkinResponses", checkinResponses);
        return responseMap;
    }

    @Override
    public void uploadImage(Long checkinId, List<MultipartFile> files) {
        Checkin checkin = this.checkinRepository.findById(checkinId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy điểm checkin"));
        for (MultipartFile file : files) {
            this.utils.isValidImage(file);
            this.utils.checkSize(file);
            String fileName = this.utils.saveImage(file);
            System.out.println(fileName);
            Image image = new Image();
            image.setUrl(fileName);
            this.imageRepository.save(image);
            CheckinImage checkinImage = new CheckinImage();
            checkinImage.setCheckin(checkin);
            checkinImage.setImage(this.imageRepository.findImageByName(fileName));
            checkinImageRepository.save(checkinImage);
        }
    }


}
