package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.CheckinDto;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private CloudinaryService cloudinaryService;

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
        int pageSize = 100;
        if (page == null || page < 1) {
            throw new IllegalArgumentException("Số trang phải lớn hơn hoặc bằng 1");
        }
        Page<Checkin> checkinPage = this.checkinRepository.findAll(PageRequest.of(page - 1, pageSize, Sort.by("createAt").descending()));
        List<CheckinResponse> checkinResponses = checkinPage.getContent()
                .stream().map(checkinMapper::toResponse).collect(Collectors.toList());

        if (checkinResponses.isEmpty()) {
            throw new AppException(ErrorType.notFound);
        }

        int totalPages = checkinPage.getTotalPages();
        long totalElements = checkinPage.getTotalElements();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("totalElements", totalElements);
        responseMap.put("totalPages", totalPages);
        responseMap.put("checkinResponses", checkinResponses);

        return responseMap;
    }

    @Override
    public Map<String, Object> getCheckinByCityName(String cityName, Integer page) {
        int pageSize = 10;
        if (page == null || page < 1) {
            throw new IllegalArgumentException("Số trang phải lớn hơn hoặc bằng 1");
        }

        Page<Checkin> checkinPage = this.checkinRepository.findByCityName(cityName, PageRequest.of(page - 1, pageSize));

        List<CheckinResponse> checkinResponses = checkinPage.getContent()
                .stream().map(checkinMapper::toResponse).collect(Collectors.toList());

        if (checkinResponses.isEmpty()) {
            throw new AppException(ErrorType.notFound);
        }

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
        if (page == null || page < 1) {
            throw new IllegalArgumentException("Số trang phải lớn hơn hoặc bằng 1");
        }

        Page<Checkin> checkinPage = this.checkinRepository.findByName(name, PageRequest.of(page - 1, pageSize));

        List<CheckinResponse> checkinResponses = checkinPage.getContent()
                .stream()
                .map(checkinMapper::toResponse)
                .collect(Collectors.toList());

        if (checkinResponses.isEmpty()) {
            throw new AppException(ErrorType.notFound);
        }

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
        // Tìm Checkin
        Checkin checkin = this.checkinRepository.findById(checkinId)
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        // Kiểm tra toàn bộ ảnh trước khi upload
        for (MultipartFile file : files) {
//            this.utils.isValidImage(file);
            this.utils.checkSize(file);
        }

        // Upload và lưu ảnh
        for (MultipartFile file : files) {
            String fileUrl;
            try {
                fileUrl = this.cloudinaryService.uploadFile(file, "").get("url").toString();
            } catch (IOException e) {
                throw new AppException(ErrorType.internalServerError);
            }

            // Lưu ảnh vào cơ sở dữ liệu
            Image image = new Image();
            image.setUrl(fileUrl);
            this.imageRepository.save(image);

            // Liên kết với CheckinImage
            CheckinImage checkinImage = new CheckinImage();
            checkinImage.setCheckin(checkin);
            checkinImage.setImage(image);
            checkinImageRepository.save(checkinImage);
        }
    }

    @Override
    public List<CheckinResponse> getCheckinRandom(Integer limit,String cityName) {
        return this.checkinRepository.findRandomCheckins(limit, cityName)
                .stream()
                .map(checkinMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Image> getImagesByCheckinId(Long checkinId) {
        return getCheckin(checkinId).getImages();
    }

    @Override
    public List<CheckinResponse> getCheckinByCityId(String cityId) {
        return this.checkinRepository.findByCityId(cityId)
                .stream()
                .map(checkinMapper::toResponse)
                .collect(Collectors.toList());
    }
}
