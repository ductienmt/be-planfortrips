package com.be_planfortrips.RoomController;

import com.be_planfortrips.controllers.RoomController;
import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.entity.TypeOfRoom;
import com.be_planfortrips.services.interfaces.IHotelService;
import com.be_planfortrips.services.interfaces.IRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private IRoomService roomService;

    @Mock
    private IHotelService hotelService;

    @InjectMocks
    private RoomController roomController;

    private RoomDto roomDto;
    private RoomResponse roomResponse;

    @BeforeEach
    void setUp() {

//        roomDto = new RoomDto();
//        roomDto.setRoomName("Deluxe Room");
//        roomDto.setTypeOfRoom(TypeOfRoom.Deluxe);
//        roomDto.setPrice(new BigDecimal("150.00"));
//        roomDto.setDescription("A spacious deluxe room");
//        roomDto.setRating(4.9);
//        roomDto.setMaxSize(2);
//        roomDto.setIsAvailable(true);


        roomResponse = RoomResponse.builder()
                .id(2l)
                .roomName("Deluxe Room")
                .typeOfRoom(TypeOfRoom.Deluxe)
                .price(new BigDecimal("150.00"))
                .description("A spacious deluxe room")
                .rating(5)
                .maxSize(2)
                .isAvailable(true)
                .hotelName("Grand Hotel")
                .images(Collections.emptyList())
                .roomAmenities(Collections.emptyList())
                .build();
    }

    @Test
    void testGetAllRooms() {

        when(roomService.getAllRoom()).thenReturn(Collections.singleton(roomResponse));


        ResponseEntity<Set<RoomResponse>> response = roomController.getAllRoom();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetRoomById() {
        when(roomService.getRoomById(2L)).thenReturn(roomResponse);

        ResponseEntity<RoomResponse> response = roomController.getRoomById(2L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roomResponse, response.getBody());
    }

    @Test
    void testCreateRoom() {
        when(roomService.createRoom(any(RoomDto.class))).thenReturn(roomResponse);
        ResponseEntity<RoomResponse> response = roomController.createRoom(roomDto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roomResponse, response.getBody());
    }

    @Test
    void testUpdateRoom() {
        when(roomService.updateRoomById(2L, roomDto)).thenReturn(roomResponse);

        ResponseEntity<RoomResponse> response = roomController.updateRoom(roomDto, 2L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roomResponse, response.getBody());
    }

    @Test
    void testDeleteRoom() {

        roomController.deleteRoom(2L);

    }
}