package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.RoomAmenitiesDto;
import com.be_planfortrips.dto.response.RoomAmenitiesResponse;
import com.be_planfortrips.entity.HotelAmenities;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.entity.RoomAmenities;
import com.be_planfortrips.repositories.RoomAmenitiesRepository;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.RoomAmenitiesMapper;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.repositories.RoomRepository;
import com.be_planfortrips.services.interfaces.IRoomAmenitiesService;
import com.be_planfortrips.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomAmenitiesServiceImpl implements IRoomAmenitiesService {
    @Autowired
    private RoomAmenitiesRepository roomAmenitiesRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomAmenitiesMapper roomAmenitiesMapper;

    @Override
    public List<RoomAmenitiesResponse> getByRoomId(Long roomId, String status) {
        Boolean s = Boolean.valueOf(status);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(ErrorType.roomIdNotFound, "Không tìm thấy room"));
        List<RoomAmenities> roomAmenities = room.getRoomAmenities();
        List<RoomAmenities> roomAmenitiesResponses = roomAmenities.stream().filter(h -> h.getStatus().equals(s)).collect(Collectors.toList());
        if (roomAmenitiesResponses == null && roomAmenities.isEmpty()) {
            throw new AppException(ErrorType.notFound, "Room không có amenities");
        }
        return roomAmenitiesResponses.stream().map(roomAmenitiesMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public RoomAmenitiesResponse create(RoomAmenitiesDto roomAmenitiesDto) {
        validateData(roomAmenitiesDto);
        Room room = roomRepository.findById(roomAmenitiesDto.getRoomId().longValue()).orElseThrow(() -> new AppException(ErrorType.roomIdNotFound, "Không tìm thấy room"));
        RoomAmenities roomAmenities = roomAmenitiesMapper.toEntity(roomAmenitiesDto);
        roomAmenitiesRepository.save(roomAmenities);
        return roomAmenitiesMapper.toResponse(roomAmenities);
    }

    @Override
    public RoomAmenitiesResponse update(Long id, RoomAmenitiesDto roomAmenitiesDto) {
        RoomAmenities roomAmenities = roomAmenitiesRepository.findById(roomAmenitiesDto.getRoomId()).orElseThrow(() -> new AppException(ErrorType.roomIdNotFound, "Không tìm thấy room"));
        roomAmenitiesMapper.updateEntityFromDto(roomAmenitiesDto, roomAmenities);
        roomAmenitiesRepository.save(roomAmenities);
        return roomAmenitiesMapper.toResponse(roomAmenities);
    }

    @Override
    public void delete(Long roomAmenitiesId) {
        RoomAmenities roomAmenities = roomAmenitiesRepository.findById(roomAmenitiesId.intValue()).orElseThrow(() -> new AppException(ErrorType.roomIdNotFound, "Không tìm thấy room"));
        roomAmenities.setStatus(false);
        roomAmenitiesRepository.save(roomAmenities);
    }

    @Override
    public void changeStatus(Long roomAmenitiesId) {
        RoomAmenities roomAmenities = roomAmenitiesRepository.findById(roomAmenitiesId.intValue()).orElseThrow(() -> new AppException(ErrorType.roomIdNotFound, "Không tìm thấy room"));
        roomAmenities.setStatus(!roomAmenities.getStatus());
        roomAmenitiesRepository.save(roomAmenities);
    }

    @Override
    public void uploadIcon(Long roomAmenitiesId, MultipartFile icon) {
        RoomAmenities roomAmenities = this.roomAmenitiesRepository.findById(roomAmenitiesId.intValue()).orElseThrow(() -> new AppException(ErrorType.roomIdNotFound, "Không tìm thấy room"));
        Utils.checkSize(icon);
        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(icon, "room_amenities");
            String imageUrl = (String) uploadResult.get("secure_url");
            Image image = new Image();
            image.setUrl(imageUrl);
            image = imageRepository.save(image);
            roomAmenities.setIcon(image);
        } catch (Exception e) {
            throw new AppException(ErrorType.UploadFailed, "Lỗi upload file");
        }
        this.roomAmenitiesRepository.save(roomAmenities);
    }

    @Override
    public void deleteIcon(Long roomAmenitiesId) {
        RoomAmenities roomAmenities = this.roomAmenitiesRepository.findById(roomAmenitiesId.intValue())
                .orElseThrow(() -> new AppException(ErrorType.roomIdNotFound, "Không tìm thấy room amenities"));

        Image icon = roomAmenities.getIcon();
        if (icon == null) {
            throw new AppException(ErrorType.notFound, "Không tìm thấy icon");
        }

        try {
            String publicId = cloudinaryService.getPublicIdFromUrl(icon.getUrl());
            cloudinaryService.deleteFile(publicId);
            imageRepository.delete(icon);
            roomAmenities.setIcon(null);
            roomAmenitiesRepository.save(roomAmenities);
        } catch (Exception e) {
            log.error("Error deleting icon: " + e.getMessage());
            throw new AppException(ErrorType.DeleteImageFailed, "Lỗi xóa icon hotel amenities");
        }
    }

    private void validateData(RoomAmenitiesDto roomAmenitiesDto) {
        if (roomAmenitiesDto.getFee() == null) {
            throw new AppException(ErrorType.inputFieldInvalid, "Phí không được để trống");
        }
        if (roomAmenitiesDto.getRoomId() == null) {
            throw new AppException(ErrorType.inputFieldInvalid, "Room id không được để trống");
        }
        if (roomAmenitiesDto.getName() == null) {
            throw new AppException(ErrorType.inputFieldInvalid, "Tên không được để trống");
        }
    }
}
