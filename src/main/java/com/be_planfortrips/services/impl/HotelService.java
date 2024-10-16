package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.HotelMapper;
import com.be_planfortrips.repositories.EnterpriseRepository;
import com.be_planfortrips.repositories.HotelRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.services.interfaces.IHotelService;
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
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class HotelService implements IHotelService {
    HotelRepository hotelRepository;
    EnterpriseRepository enterpriseRepository;
    ImageRepository imageRepository;
    HotelMapper hotelMapper;
    CloudinaryService cloudinaryService;
    @Override
    @Transactional
    public HotelResponse createHotel(HotelDto hotelDto) throws Exception {
        AccountEnterprise accountEnterprise = enterpriseRepository.findById(hotelDto.getEnterpriseId())
                .orElseThrow(()->new Exception("Not found"));
        Hotel hotel = hotelMapper.toEntity(hotelDto);
        hotel.setAccountEnterprise(accountEnterprise);
        hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse updateHotel(Long id, HotelDto hotelDto) throws Exception {
        Hotel existHotel = hotelRepository.findById(id)
                           .orElseThrow(()->new Exception("Not found"));
        AccountEnterprise accountEnterprise = enterpriseRepository.findById(hotelDto.getEnterpriseId())
                .orElseThrow(()->new Exception("Not found"));
        hotelMapper.updateEntityFromDto(hotelDto,existHotel);
        existHotel.setId(id);
        existHotel.setAccountEnterprise(accountEnterprise);
        hotelRepository.saveAndFlush(existHotel);
        return hotelMapper.toResponse(existHotel);
    }

    @Override
    public Page<HotelResponse> getHotels(PageRequest request) {
        return hotelRepository.findAll(request).map(hotel -> hotelMapper.toResponse(hotel));
    }

    @Override
    public HotelResponse getByHotelId(Long id) throws Exception {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(()->new Exception("Not found"));
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Optional<Hotel> orderOptional = hotelRepository.findById(id);
        orderOptional.ifPresent(hotelRepository::delete);
    }

    @Override
    @Transactional
    public HotelResponse createHotelImage(Long hotelId, List<MultipartFile> files) throws Exception {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new Exception("Hotel not found"));

        if (files == null || files.isEmpty()) {
            throw new Exception("No files to upload");
        }

        List<Image> imageList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new Exception("One or more files are empty");
            }
            if (file.getSize() > 10 * 1024 * 1024) { // Giới hạn 10MB
                throw new Exception("File " + file.getOriginalFilename() + " is too large! Maximum size is 10MB");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new Exception("File " + file.getOriginalFilename() + " must be an image");
            }

            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, "hotels");
            String imageUrl = (String) uploadResult.get("secure_url");
            Image image = new Image();
            image.setUrl(imageUrl);
            image = imageRepository.saveAndFlush(image);
            imageList.add(image);
        }
        hotel.setImages(imageList);
        hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse deleteImage(Long id, List<Integer> imageIds) throws Exception {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );
        List<Image> images = hotel.getImages();
        System.out.println("Total images for hotel ID " + hotel.getId() + ": " + images.size());

        List<Image> imagesToDelete = images.stream()
                .filter(image -> imageIds.contains(Integer.valueOf(String.valueOf(image.getId()))))
                .collect(Collectors.toList());
        if (imagesToDelete.isEmpty()) {
            System.out.println("No images found to delete.");
        } else {
            System.out.println("Images to delete: " + imagesToDelete.size());
        }
        if (imagesToDelete.isEmpty()) {
            throw new Exception("No images found to delete");
        }

        for (Image image : imagesToDelete) {
            try {
                String publicId = cloudinaryService.getPublicIdFromUrl(image.getUrl());
                cloudinaryService.deleteFile(publicId);
                hotel.getImages().remove(image);
                imageRepository.delete(image);
            } catch (Exception e) {
                throw new Exception("Error deleting image: " + e.getMessage());
            }
        }
        hotelRepository.saveAndFlush(hotel);
        return hotelMapper.toResponse(hotel);
    }
}
