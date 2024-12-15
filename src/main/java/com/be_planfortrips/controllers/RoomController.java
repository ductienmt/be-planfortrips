package com.be_planfortrips.controllers;


import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.services.interfaces.IHotelService;
import com.be_planfortrips.services.interfaces.IRoomService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${api.prefix}/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {

    IRoomService roomService;

    IHotelService hotelService;

    @GetMapping("/all")
    public ResponseEntity<Set<RoomResponse>> getAllRoom() {
        Set<RoomResponse> responses = roomService.getAllRoom();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("getById/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(
            @PathVariable Long roomId
    ) {
        RoomResponse response = roomService.getRoomById(roomId);
        return ResponseEntity.ok(response);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/create")
    public ResponseEntity<RoomResponse> createRoom(
            @RequestBody @Valid RoomDto roomDto
    ) {
        RoomResponse response = roomService.createRoom(roomDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-image/{roomId}")
    public ResponseEntity<?> uploadImageRoomById(
            @RequestParam("files") List<MultipartFile> files,
            @PathVariable Long roomId) {
        try {
            boolean isSuccess = roomService.uploadImageRoomById(files, roomId);
            return ResponseEntity.ok().body(isSuccess);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
        }
    }

    @PutMapping("update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @RequestBody RoomDto roomDto, @PathVariable Long roomId
    ) {
        RoomResponse response = roomService.updateRoomById(roomId, roomDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long roomId
    ) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }

    // test this endpoint, don't care about the response
    @GetMapping("/getRoomAvailable")
    public ResponseEntity<?> getRoomAvailable(
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime checkIn,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime checkOut
    ) {
        return ResponseEntity.ok(hotelService.getRoomAvailable(checkIn, checkOut, destination));
    }

    @GetMapping("/getRoomAvailableByHotelId")
    public ResponseEntity<?> getRoomAvailableByHotelId(
            @RequestParam Long hotelId,
            @RequestParam Integer status,
            @RequestParam(defaultValue = "0", required = false) Integer pageNo,
            @RequestParam(defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String sortType

    ) {
        return ResponseEntity.ok(roomService.getRoomByStatus(hotelId, status, pageNo, pageSize, sortBy, sortType));
    }

    @GetMapping("/getRoomByHotelId")
    public ResponseEntity<?> getRoomByHotelId(@RequestParam Long id,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate,
                                              @RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                              @RequestParam(defaultValue = "5", required = false) Integer pageSize,
                                              @RequestParam(defaultValue = "id", required = false) String sortBy,
                                              @RequestParam(defaultValue = "asc", required = false) String sortType) {
        return ResponseEntity.ok(roomService.getRoomByHotelId(id,checkinDate, checkoutDate, pageNo, pageSize, sortBy, sortType));
    }

    @GetMapping("/getRoomByEnterpriseId")
    public ResponseEntity<?> getRoomByEnterpriseId() {
        return ResponseEntity.ok(roomService.getRoomByEnterpriseId());
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterRoom(
            @RequestParam Integer hotelId, @RequestParam(required = false) Integer status, @RequestParam(required = false) String roomType,
            @RequestParam(defaultValue = "0", required = false) Integer pageNo,
            @RequestParam(defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String sortType
    ) {
        return ResponseEntity.ok(roomService.filterRoom(hotelId.longValue(), status, roomType, pageNo, pageSize, sortBy, sortType));
    }

}
