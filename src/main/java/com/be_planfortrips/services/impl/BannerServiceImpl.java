package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.BannerDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.BannerResponseAdmin;
import com.be_planfortrips.dto.response.BannerResponseUser;
import com.be_planfortrips.entity.Banner;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.BannerMapper;
import com.be_planfortrips.mappers.impl.BannerUserMapper;
import com.be_planfortrips.repositories.BannerRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.services.interfaces.IBannerService;
import com.be_planfortrips.services.interfaces.ICloudinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BannerServiceImpl implements IBannerService {
    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private BannerUserMapper bannerUserMapper;

    @Autowired
    private ICloudinaryService cloudinaryService;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public BannerResponseAdmin createBanner(BannerDto bannerDto) {
        Banner banner = bannerMapper.toEntity(bannerDto);
        this.bannerRepository.save(banner);
        return bannerMapper.toResponse(banner);
    }

    @Override
    public BannerResponseAdmin updateBanner(Long id, BannerDto bannerDto) {
        Banner banner = this.bannerRepository.findById(id).orElseThrow(() -> new AppException(ErrorType.notFound));
        bannerMapper.updateEntityFromDto(bannerDto, banner);
        this.bannerRepository.saveAndFlush(banner);
        return bannerMapper.toResponse(banner);
    }

    @Override
    public void deleteBanner(Long id) {
        Banner banner = this.bannerRepository.findById(id).orElseThrow(() -> new AppException(ErrorType.notFound));
        banner.setIsActive(false);
        this.bannerRepository.save(banner);
    }

    @Override
    public BannerResponseAdmin getBannerById(Long id) {
        return this.bannerMapper.toResponse(this.bannerRepository.findById(id).orElseThrow(() -> new AppException(ErrorType.notFound)));
    }

    @Override
    public List<BannerResponseAdmin> getAllBanners() {
        return this.bannerRepository.findAll().stream().map(bannerMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<BannerResponseUser> getAllBannersUser() {
        return this.bannerRepository.findAllUser().stream().map(bannerUserMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public void changeStatusBanner(Long id, Integer status) {
        Banner banner = this.bannerRepository.findById(id).orElseThrow(() -> new AppException(ErrorType.notFound));
        if (status == 1) {
            banner.setIsActive(true);
        } else {
            banner.setIsActive(false);
        }
        this.bannerRepository.saveAndFlush(banner);
    }

    @Override
    public void uploadBannerImage(Long id, MultipartFile file) {
        // Tìm Banner bằng id
        Banner banner = this.bannerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        Map<String, Object> uploadResult = null;

        try {
            // Upload hình ảnh lên Cloudinary
            uploadResult = cloudinaryService.uploadFile(file, "");

            // Kiểm tra kết quả upload
            if (uploadResult == null || !uploadResult.containsKey("url")) {
                throw new AppException(ErrorType.internalServerError);
            }

        } catch (IOException e) {
            log.error("Error uploading banner image for banner id {}: {}", id, e.getMessage());
            throw new AppException(ErrorType.internalServerError);
        }

        // Lấy URL của hình ảnh từ kết quả upload
        String imageUrl = uploadResult.get("url").toString();

        // Tạo đối tượng Image và lưu vào database
        Image image = new Image();
        image.setUrl(imageUrl);

        try {
            // Lưu đối tượng Image trước khi liên kết với Banner
            this.imageRepository.save(image);
        } catch (Exception e) {
            log.error("Error saving image for banner id {}: {}", id, e.getMessage());
            throw new AppException(ErrorType.internalServerError);
        }

        // Liên kết image với banner
        banner.setImage(image);

        // Lưu lại banner vào database
        try {
            this.bannerRepository.saveAndFlush(banner);
        } catch (Exception e) {
            log.error("Error saving banner with image id {}: {}", image.getId(), e.getMessage());
            throw new AppException(ErrorType.internalServerError);
        }
    }

}
