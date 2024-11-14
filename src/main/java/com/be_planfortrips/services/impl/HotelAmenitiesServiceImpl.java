package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.HotelAmenitiesDto;
import com.be_planfortrips.dto.response.HotelAmenitiesResponse;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.HotelAmenities;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.repositories.HotelAmenitiesRepository;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.HotelAmenitiesMapper;
import com.be_planfortrips.repositories.HotelRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.services.interfaces.IHotelAmenitiesService;
import com.be_planfortrips.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HotelAmenitiesServiceImpl implements IHotelAmenitiesService {

    @Autowired
    private HotelAmenitiesMapper hotelAmenitiesMapper;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private HotelAmenitiesRepository hotelAmenitiesRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<HotelAmenitiesResponse> getByHotelId(Long hotelId, String status) {
        Boolean s = Boolean.valueOf(status);
        Hotel hotel = this.hotelRepository.findById(hotelId).orElseThrow(() -> new AppException(ErrorType.HotelIdNotFound, "Không tìm thấy hotel"));
        List<HotelAmenities> hotelAmenities = hotel.getHotelAmenities();
        List<HotelAmenities> hotelAmenitiesResponses = hotelAmenities.stream().filter(h -> h.getStatus().equals(s)).collect(Collectors.toList());
        if (hotelAmenitiesResponses == null && hotelAmenitiesResponses.isEmpty()) {
            throw new AppException(ErrorType.notFound, "Hotel không có amenities");
        }
        return hotelAmenitiesResponses.stream().map(this.hotelAmenitiesMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public HotelAmenitiesResponse create(HotelAmenitiesDto hotelAmenitiesDto) {
        validateData(hotelAmenitiesDto);
        Hotel hotel = this.hotelRepository.findById(hotelAmenitiesDto.getHotelId()).orElseThrow(() -> new AppException(ErrorType.HotelIdNotFound, "Không tìm thấy hotel"));
        HotelAmenities hotelAmenities = this.hotelAmenitiesMapper.toEntity(hotelAmenitiesDto);
        this.hotelAmenitiesRepository.save(hotelAmenities);
        return this.hotelAmenitiesMapper.toResponse(hotelAmenities);
    }

    @Override
    public HotelAmenitiesResponse update(Long id, HotelAmenitiesDto hotelAmenitiesDto) {
        HotelAmenities hotelAmenities = this.hotelAmenitiesRepository.findById(id.intValue()).orElseThrow(() -> new AppException(ErrorType.HotelIdNotFound, "Không tìm thấy hotel"));
        this.hotelAmenitiesMapper.updateEntityFromDto(hotelAmenitiesDto, hotelAmenities);
        this.hotelAmenitiesRepository.save(hotelAmenities);
        return this.hotelAmenitiesMapper.toResponse(hotelAmenities);
    }

    @Override
    public void delete(Long hotelAmenitiesId) {
        HotelAmenities hotelAmenities = this.hotelAmenitiesRepository.findById(hotelAmenitiesId.intValue()).orElseThrow(() -> new AppException(ErrorType.HotelIdNotFound, "Không tìm thấy hotel"));
        hotelAmenities.setStatus(false);
        this.hotelAmenitiesRepository.save(hotelAmenities);
    }

    @Override
    public void changeStatus(Long hotelAmenitiesId) {
        HotelAmenities hotelAmenities = this.hotelAmenitiesRepository.findById(hotelAmenitiesId.intValue()).orElseThrow(() -> new AppException(ErrorType.HotelIdNotFound, "Không tìm thấy hotel"));
        hotelAmenities.setStatus(!hotelAmenities.getStatus());
        this.hotelAmenitiesRepository.save(hotelAmenities);
    }

    @Override
    public void uploadIcon(Long hotelAmenitiesId, MultipartFile icon) {
        HotelAmenities hotelAmenities = this.hotelAmenitiesRepository.findById(hotelAmenitiesId.intValue()).orElseThrow(() -> new AppException(ErrorType.HotelIdNotFound, "Không tìm thấy hotel"));
        Utils.checkSize(icon);
        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(icon, "hotel_amenities");
            String imageUrl = (String) uploadResult.get("secure_url");
            Image image = new Image();
            image.setUrl(imageUrl);
            image = imageRepository.save(image);
            hotelAmenities.setIcon(image);
        } catch (Exception e) {
            throw new AppException(ErrorType.UploadFailed, "Lỗi upload file");
        }
        this.hotelAmenitiesRepository.save(hotelAmenities);
    }

    @Override
    @Transactional
    public void deleteIcon(Long hotelAmenitiesId) {
        HotelAmenities hotelAmenities = this.hotelAmenitiesRepository.findById(hotelAmenitiesId.intValue())
                .orElseThrow(() -> new AppException(ErrorType.HotelIdNotFound, "Không tìm thấy hotel amenities"));

        Image icon = hotelAmenities.getIcon();
        if (icon == null) {
            throw new AppException(ErrorType.notFound, "Không tìm thấy icon");
        }

        try {
            String publicId = cloudinaryService.getPublicIdFromUrl(icon.getUrl());
            cloudinaryService.deleteFile(publicId);
            imageRepository.delete(icon);
            hotelAmenities.setIcon(null);
            hotelAmenitiesRepository.save(hotelAmenities);
        } catch (Exception e) {
            log.error("Error deleting icon: " + e.getMessage());
            throw new AppException(ErrorType.DeleteImageFailed, "Lỗi xóa icon hotel amenities");
        }
    }

    private void validateData(HotelAmenitiesDto hotelAmenitiesDto) {
        if (hotelAmenitiesDto.getFee() == null) {
            throw new AppException(ErrorType.inputFieldInvalid, "Phí không được để trống");
        }
        if (hotelAmenitiesDto.getHotelId() == null) {
            throw new AppException(ErrorType.inputFieldInvalid, "Hotel id không được để trống");
        }
        if (hotelAmenitiesDto.getName() == null) {
            throw new AppException(ErrorType.inputFieldInvalid, "Tên không được để trống");
        }
    }


}
