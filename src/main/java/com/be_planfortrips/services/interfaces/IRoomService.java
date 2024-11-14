package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.dto.response.RoomResponseEnterprise;
import com.be_planfortrips.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    List<RoomResponse> getRoomByHotelId(Long id);

    List<RoomResponse> getRoomAvailable(LocalDateTime checkIn, LocalDateTime checkOut, String destination);

    boolean uploadImageRoomById(List<MultipartFile> file, Long roomId) throws IOException;

}
