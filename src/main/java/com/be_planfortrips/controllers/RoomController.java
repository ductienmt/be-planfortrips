package com.be_planfortrips.controllers;


import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.services.interfaces.IRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Set;

@RestController
@RequestMapping("${api.prefix}/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@CrossOrigin(origins = "http://localhost:5050/")
public class RoomController {

    IRoomService roomService;

    @GetMapping
    public ResponseEntity<Set<RoomResponse>> getAllRoom() {
        Set<RoomResponse> responses = roomService.getAllRoom();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(
            @PathVariable Long roomId
            ) {
        RoomResponse response = roomService.getRoomById(roomId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomResponse> createRoom(
           @RequestBody RoomDto roomDto
    ) {
        RoomResponse response = roomService.createRoom(roomDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @RequestBody RoomDto roomDto, @PathVariable Long roomId
    )  {
        RoomResponse response = roomService.updateRoomById(roomId, roomDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long roomId
    ) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }


}
