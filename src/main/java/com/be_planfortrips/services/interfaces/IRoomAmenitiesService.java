package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.RoomAmenitiesDto;
import com.be_planfortrips.dto.response.RoomAmenitiesResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IRoomAmenitiesService {
    List<RoomAmenitiesResponse> getByRoomId(Long roomId, String status);
    RoomAmenitiesResponse create(RoomAmenitiesDto roomAmenitiesDto);
    RoomAmenitiesResponse update(Long id, RoomAmenitiesDto roomAmenitiesDto);
    void delete(Long roomAmenitiesId);
    void changeStatus(Long roomAmenitiesId);
    void uploadIcon(Long roomAmenitiesId, MultipartFile icon);
    void deleteIcon(Long roomAmenitiesId);
}
