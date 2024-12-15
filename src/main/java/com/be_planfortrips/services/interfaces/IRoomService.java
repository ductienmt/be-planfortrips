package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.dto.response.RoomResponseEnterprise;
import com.be_planfortrips.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRoomService {

    Set<RoomResponse> getAllRoom();

    RoomResponse getRoomById(Long roomId);

    RoomResponse createRoom(RoomDto roomDto);

    void deleteRoomById(Long roomId);

    RoomResponse updateRoomById(Long roomId, RoomDto roomDto);

    Page<RoomResponse> getRoomByHotelId(Long id, LocalDate checkinDate, LocalDate checkoutDate, Integer pageNo, Integer pageSize, String sortBy, String sortType);

    List<RoomResponse> getRoomAvailable(LocalDateTime checkIn, LocalDateTime checkOut, String destination);

    boolean uploadImageRoomById(List<MultipartFile> file, Long roomId) throws IOException;

    Page<RoomResponseEnterprise> getRoomByStatus(Long hotelId, Integer status, Integer pageNo, Integer pageSize, String sortBy, String sortType);

    Page<RoomResponseEnterprise> filterRoom(Long hotelId, Integer status, String roomType, Integer pageNo, Integer pageSize, String sortBy, String sortType);
    Set<Map<String, Object>> getRoomByEnterpriseId();



}
