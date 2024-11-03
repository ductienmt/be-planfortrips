package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.entity.RoomImage;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.mappers.impl.RoomMapper;
import com.be_planfortrips.repositories.RoomRepository;
import com.be_planfortrips.services.interfaces.ICloudinaryService;
import com.be_planfortrips.services.interfaces.IRoomService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class RoomServiceImpl implements IRoomService {

    RoomRepository roomRepository;
    MapperInterface<RoomResponse, Room, RoomDto> roomMapper;
    ICloudinaryService cloudinaryService;

    @Override
    public Set<RoomResponse> getAllRoom() {
        List<RoomResponse> responses = roomRepository.findAll().stream().map(roomMapper::toResponse).toList();
        return new HashSet<>(responses);
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room roomExist = roomRepository.findById(roomId).orElseThrow();
        return roomMapper.toResponse(roomExist);
    }

    @Override
    public RoomResponse createRoom(RoomDto roomDto) {
        Room roomCreate = roomMapper.toEntity(roomDto);
        return roomMapper.toResponse(roomRepository.save(roomCreate));
    }

    @Override
    public void deleteRoomById(Long roomId) {
        Room roomCreate = roomRepository.findById(roomId).orElseThrow();
        roomRepository.deleteById(roomId);
    }

    @Override
    public RoomResponse updateRoomById(Long roomId, RoomDto roomDto) {
        roomRepository.findById(roomId).orElseThrow();
        Room room = roomMapper.toEntity(roomDto);
        room.setId(roomId);
        return roomMapper.toResponse(roomRepository.save(room));
    }

    @Override
    public List<RoomResponse> getRoomByHotelId(Long hotelId) {
        return this.roomRepository.findByHotelId(hotelId).stream().map(roomMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getRoomAvailable(LocalDateTime checkIn, LocalDateTime checkOut) {
        List<Room> availableRooms = roomRepository.findAvailableRooms(checkIn, checkOut);

        List<RoomResponse> roomResponses = availableRooms.stream()
                .map(roomMapper::toResponse)
                .collect(Collectors.toList());

        for (RoomResponse roomResponse : roomResponses) {
            System.out.println(roomResponse.getRoomName() + " " + roomResponse.getHotel().getName());
        }
        return roomResponses;
    }

    @Override
    public Image uploadImageRoomById(MultipartFile file, Long roomId) throws IOException {
        // Check RoomId exist
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new AppException(ErrorType.roomIdNotFound)
        );

        // Gọi Service Cloudinary
        Map<String,Object> result = cloudinaryService.uploadFile(file, "Room");
        if (result.isEmpty()) {
            throw new AppException(ErrorType.UploadFailed);
        }

        // Result Exist

        // Get RoomImage Existed in Room
        List<RoomImage> roomImages = room.getImages();

        // Create Image
        Image image = Image.builder().build();
        image.setUrl(result.get("url").toString());

        RoomImage roomImage = RoomImage.builder().build();
        roomImages.add(RoomImage.builder()
                .room(room)
                .image(image).build());
        // Add RoomImage To RoomImages
        roomImages.add(roomImage);

        // Save Room To Repository
        roomRepository.save(room);

        return image;
    }


}
