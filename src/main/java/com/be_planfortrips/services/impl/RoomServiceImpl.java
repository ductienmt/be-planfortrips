package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.dto.response.RoomResponseEnterprise;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.mappers.impl.PageMapperImpl;
import com.be_planfortrips.mappers.impl.RoomMapper;
import com.be_planfortrips.mappers.impl.RoomMapper_2;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.interfaces.ICloudinaryService;
import com.be_planfortrips.services.interfaces.IRoomService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class RoomServiceImpl implements IRoomService {

    RoomRepository roomRepository;
    MapperInterface<RoomResponse, Room, RoomDto> roomMapper;
    ICloudinaryService cloudinaryService;
    TokenMapperImpl tokenMapperImpl;
    RoomMapper_2 roomMapper_2;
    PageMapperImpl pageMapperImpl;
    private final HotelRepository hotelRepository;
    private final BookingHotelDetailRepository bookingHotelDetailRepository;
    private final RoomImageRepository roomImageRepository;
    ImageRepository imageRepository;

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
        boolean isRoomInBooking = bookingHotelDetailRepository.existsByRoomId(roomId);
        if (isRoomInBooking) {
            Room roomCreate = roomRepository.findById(roomId).orElseThrow();
            roomCreate.setAvailable(false);
            roomRepository.save(roomCreate);
        } else {
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public RoomResponse updateRoomById(Long roomId, RoomDto roomDto) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );
        for (Field field : RoomDto.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object newValue = field.get(roomDto);

                if (newValue != null) {
                    if (field.getName().equals("hotelId")) {
                        Long hotelId = (Long) newValue;
                        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                                () -> new AppException(ErrorType.notFound, "Khách sạn không tồn tại")
                        );
                        room.setHotel(hotel);
                    } else {
                        try {
                            Field serviceField = Room.class.getDeclaredField(field.getName());
                            serviceField.setAccessible(true);
                            serviceField.set(room, newValue);
                        } catch (NoSuchFieldException e) {
                            continue;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing field: " + field.getName(), e);
            }
        }
        return roomMapper.toResponse(roomRepository.save(room));
    }

    @Override
    public Page<RoomResponse> getRoomByHotelId(Long id, LocalDate checkinDate, LocalDate checkoutDate, Integer pageNo, Integer pageSize, String sortBy, String sortType) {
        var pageable = pageMapperImpl.customPage(pageNo, pageSize, sortBy, sortType);
        
        return this.roomRepository.findByHotelId(id,checkinDate,checkoutDate, pageable).map(roomMapper::toResponse);
    }

    @Override
    public List<RoomResponse> getRoomAvailable(LocalDateTime checkIn, LocalDateTime checkOut, String destination) {
        List<Room> availableRooms = roomRepository.findAvailableRoomsNew(checkIn, checkOut, destination);

        List<RoomResponse> roomResponses = availableRooms.stream()
                .map(roomMapper::toResponse)
                .collect(Collectors.toList());

//        for (RoomResponse roomResponse : roomResponses) {
//            System.out.println(roomResponse.getRoomName() + " " + ro);
//        }
        return roomResponses;
    }




    @Override
    public boolean uploadImageRoomById(List<MultipartFile> files, Long roomId) throws IOException {
        // Check RoomId exist
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new AppException(ErrorType.roomIdNotFound)
        );

        // Get RoomImage list for the Room
        List<RoomImage> roomImages = room.getImages();

        // Iterate over each file to upload
        for (MultipartFile file : files) {
            // Gọi Service Cloudinary cho mỗi file
            Map<String, Object> result = cloudinaryService.uploadFile(file, "Room");
            if (result.isEmpty()) {
                throw new AppException(ErrorType.UploadFailed);
            }

            // Create Image object and set URL
            Image image = Image.builder().build();
            image.setUrl(result.get("url").toString());

            imageRepository.save(image);

            // Create RoomImage object and link it to the room
            RoomImage roomImage = RoomImage.builder()
                    .room(room)
                    .image(image)
                    .build();

            // Add the new RoomImage to the list of images
            roomImages.add(roomImage);
            roomImageRepository.save(roomImage);
            room.setImages(roomImages);
        }

        // Save the Room with updated images
        roomRepository.save(room);

        // Return true to indicate successful upload of all files
        return true;
    }

    @Override
    public Page<RoomResponseEnterprise> getRoomByStatus(Long hotelId, Integer status, Integer pageNo, Integer pageSize, String sortBy, String sortType) {
        if (status != 0 && status != 1) {
            throw new AppException(ErrorType.statusInvalid);
        }
        var pageable = pageMapperImpl.customPage(pageNo, pageSize, sortBy, sortType);
        LocalDateTime currentDate = LocalDateTime.now();
        Page<Room> rooms = Page.empty();
        if (status == 1) {
            rooms = this.roomRepository.findAvailableRoomsByHotelIdAndDate(hotelId, currentDate,pageable);
        }
        if (status == 0){
            rooms = this.roomRepository.findBookedRoomsByHotelIdAndDate(hotelId, currentDate,pageable);
        }

        return rooms.map(roomMapper_2::toResponse);

    }

    @Override
    public Page<RoomResponseEnterprise> filterRoom(Long hotelId, Integer status, String roomType, Integer pageNo, Integer pageSize, String sortBy, String sortType) {
        var pageable = pageMapperImpl.customPage(pageNo, pageSize, sortBy, sortType);

        Boolean isAvailable = null;
        if (status != null) {
            isAvailable = status == 1;
        }

        TypeOfRoom typeOfRoom = null;
        if (roomType != null && !roomType.isEmpty()) {
            try {
                typeOfRoom = TypeOfRoom.valueOf(roomType);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Loại phòng không hợp lệ: " + roomType);
            }
        }

        Page<Room> rooms = roomRepository.filterRoom(hotelId, isAvailable, typeOfRoom, pageable);

        return rooms.map(roomMapper_2::toResponse);
    }

    @Override
    public Set<Map<String, Object>> getRoomByEnterpriseId() {
        List<Room> rooms = roomRepository.findByEnterpriseId(tokenMapperImpl.getIdEnterpriseByToken());

        Set<Map<String, Object>> response = new TreeSet<>(
                Comparator.comparingLong(map -> (long) map.get("roomId"))
        );

        for (Room room : rooms) {
            Map<String, Object> roomMap = new HashMap<>();
            roomMap.put("roomId", room.getId());
            roomMap.put("roomName", room.getRoomName());
            response.add(roomMap);
        }

        return response;
    }



}
