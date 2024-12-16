package com.be_planfortrips.Test.anhquan;

import com.be_planfortrips.dto.RoomAmenitiesDto;
import com.be_planfortrips.dto.response.RoomAmenitiesResponse;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.entity.RoomAmenities;
import com.be_planfortrips.mappers.impl.RoomAmenitiesMapper;
import com.be_planfortrips.repositories.RoomAmenitiesRepository;
import com.be_planfortrips.repositories.RoomRepository;
import com.be_planfortrips.services.impl.CloudinaryService;
import com.be_planfortrips.services.impl.RoomAmenitiesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import com.be_planfortrips.exceptions.AppException;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomAmenitiesServiceImplTest {

    @Mock
    private RoomAmenitiesRepository roomAmenitiesRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomAmenitiesMapper roomAmenitiesMapper;

    @Mock
    private MultipartFile icon;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private RoomAmenitiesServiceImpl roomAmenitiesService;

    private Room room;
    private RoomAmenities roomAmenities;
    private RoomAmenitiesDto roomAmenitiesDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tạo đối tượng Room và RoomAmenities giả lập
        room = new Room();
        room.setId(1L);

        roomAmenities = new RoomAmenities();
        roomAmenities.setId(1);
        roomAmenities.setRoom(room);
        roomAmenities.setName("Gym");
        roomAmenities.setFee(BigDecimal.valueOf(50));

        roomAmenitiesDto = new RoomAmenitiesDto();
        roomAmenitiesDto.setRoomId(1);
        roomAmenitiesDto.setName("Gym");
        roomAmenitiesDto.setFee(BigDecimal.valueOf(50));
    }

    @Test
    void testCreate_Success() {
        // Giả lập phương thức findById của RoomRepository
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        // Giả lập phương thức save của RoomAmenitiesRepository
        when(roomAmenitiesRepository.save(any(RoomAmenities.class))).thenReturn(roomAmenities);

        // Giả lập mapper
        when(roomAmenitiesMapper.toEntity(roomAmenitiesDto)).thenReturn(roomAmenities);
        when(roomAmenitiesMapper.toResponse(roomAmenities)).thenReturn(new RoomAmenitiesResponse());

        // Kiểm tra phương thức create
        RoomAmenitiesResponse response = roomAmenitiesService.create(roomAmenitiesDto);

        // Assert
        assertNotNull(response);
        verify(roomAmenitiesRepository, times(1)).save(any(RoomAmenities.class));
    }

    @Test
    void testGetByRoomId() {
        // Arrange
        Room room = new Room();
        RoomAmenities roomAmenity = new RoomAmenities();
        roomAmenity.setStatus(true);  // Hoặc set null để kiểm tra trường hợp status là null
        room.setRoomAmenities(Arrays.asList(roomAmenity));  // Đảm bảo roomAmenities không phải null

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        // Act
        List<RoomAmenitiesResponse> response = roomAmenitiesService.getByRoomId(1L, "true");

        // Assert
        assertNotNull(response);
        verify(roomRepository, times(1)).findById(1L);
    }



    @Test
    void testUpdate_Success() {
        // Giả lập phương thức findById của RoomAmenitiesRepository
        when(roomAmenitiesRepository.findById(1)).thenReturn(Optional.of(roomAmenities));

        // Giả lập phương thức mapper update
        doNothing().when(roomAmenitiesMapper).updateEntityFromDto(roomAmenitiesDto, roomAmenities);

        // Giả lập phương thức save của RoomAmenitiesRepository
        when(roomAmenitiesRepository.save(roomAmenities)).thenReturn(roomAmenities);

        // Giả lập phương thức chuyển đổi sang response
        when(roomAmenitiesMapper.toResponse(roomAmenities)).thenReturn(new RoomAmenitiesResponse());

        // Kiểm tra phương thức update
        RoomAmenitiesResponse response = roomAmenitiesService.update(1L, roomAmenitiesDto);

        // Assert
        assertNotNull(response);
        verify(roomAmenitiesRepository, times(1)).save(roomAmenities);
    }

    @Test
    void testDelete_Success() {
        // Giả lập phương thức findById của RoomAmenitiesRepository
        when(roomAmenitiesRepository.findById(1)).thenReturn(Optional.of(roomAmenities));

        // Kiểm tra phương thức delete
        roomAmenitiesService.delete(1L);

        // Assert
        verify(roomAmenitiesRepository, times(1)).save(roomAmenities);
    }

    @Test
    void testChangeStatus_Success() {
        // Arrange
        RoomAmenities roomAmenities = new RoomAmenities();
        roomAmenities.setStatus(true);  // Hoặc false tùy theo mong muốn của bạn

        // Giả lập việc roomAmenities được tìm thấy trong repository
        when(roomAmenitiesRepository.findById(1)).thenReturn(Optional.of(roomAmenities));

        // Kiểm tra phương thức changeStatus
        roomAmenitiesService.changeStatus(1L);

        // Assert
        verify(roomAmenitiesRepository, times(1)).save(roomAmenities);  // Kiểm tra save đã được gọi
        assertNotNull(roomAmenities.getStatus());  // Kiểm tra rằng status không phải null sau khi thay đổi
        assertFalse(roomAmenities.getStatus());  // Kiểm tra giá trị status sau khi thay đổi (có thể là true hoặc false tùy vào logic của bạn)
    }



//    @Test
//    void testUploadIcon_Success() throws IOException {
//        // Giả lập phương thức findById của RoomAmenitiesRepository
//        when(roomAmenitiesRepository.findById(1)).thenReturn(Optional.of(roomAmenities));
//
//        // Giả lập phương thức checkSize của Utils (method checkSize là void)
//        doNothing().when(Utils.class);  // Không trả về gì, chỉ thực hiện hành động
//        doNothing().when(utils).checkSize(any(MultipartFile.class));  // Giả lập phương thức checkSize
//
//        // Giả lập phương thức upload của cloudinaryService
//        Map<String, Object> uploadResult = Map.of("secure_url", "http://example.com/image.jpg");
//        when(cloudinaryService.uploadFile(any(), eq("room_amenities"))).thenReturn(uploadResult);
//
//        // Tạo đối tượng MultipartFile giả lập
//        MockMultipartFile icon = new MockMultipartFile("file", "icon.jpg", "image/jpeg", new byte[0]);
//
//        // Kiểm tra phương thức uploadIcon
//        roomAmenitiesService.uploadIcon(1L, icon);
//
//        // Assert
//        verify(roomAmenitiesRepository, times(1)).save(roomAmenities);
//    }



    @Test
    public void testDeleteIcon_Fail_NotFound() {
        // Arrange (Chuẩn bị)
        Long roomAmenitiesId = 1L;

        // Giả lập trường hợp không tìm thấy RoomAmenities
        when(roomAmenitiesRepository.findById(roomAmenitiesId.intValue())).thenReturn(Optional.empty());

        // Kiểm tra khi gọi phương thức deleteIcon sẽ ném ra ngoại lệ
        assertThrows(AppException.class, () -> roomAmenitiesService.deleteIcon(roomAmenitiesId));
    }
}

