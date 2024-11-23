package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.RoomAmenitiesDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.RoomAmenitiesResponse;
import com.be_planfortrips.services.interfaces.IRoomAmenitiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/room-amenities")
@Slf4j
public class RoomAmenitiesController {
    @Autowired
    private IRoomAmenitiesService roomAmenitiesService;

    // Phương thức hỗ trợ để tạo ApiResponse
    private ResponseEntity<ApiResponse<Void>> buildApiResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                ApiResponse.<Void>builder()
                        .code(status.value())
                        .message(message)
                        .build()
        );
    }

    @GetMapping("/get-by-room-id")
    public ResponseEntity<?> getByHotelId(@RequestParam Long roomId, @RequestParam(defaultValue = "true", required = false) String status) {
        try {
            if (roomId == null) {
                throw new Exception("roomId id is required");
            }
            return ResponseEntity.ok(
                    ApiResponse.<List<RoomAmenitiesResponse>>builder()
                            .code(HttpStatus.OK.value())
                            .data(this.roomAmenitiesService.getByRoomId(roomId, status))
                            .message("Lấy danh sách tiện ích thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Lấy danh sách tiện ích thất bại.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RoomAmenitiesDto roomAmenitiesDto) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.<RoomAmenitiesResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(this.roomAmenitiesService.create(roomAmenitiesDto))
                            .message("Tạo tiện ích thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody RoomAmenitiesDto roomAmenitiesDto) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.<RoomAmenitiesResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(this.roomAmenitiesService.update(id, roomAmenitiesDto))
                            .message("Cập nhật tiện ích thành công.")
                            .build()
            );
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Cập nhật tiện ích thất bại.");
        }
    }

    @PatchMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        try {
            this.roomAmenitiesService.delete(id);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Xóa tiện ích thành công.")
                            .build()
            );
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Xóa tiện ích thất bại.");
        }
    }

    @PatchMapping("/change-status")
    public ResponseEntity<?> changeStatus(@RequestParam Long id) {
        try {
            this.roomAmenitiesService.changeStatus(id);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Thay đổi trạng thái tiện ích thành công.")
                            .build()
            );
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Thay đổi trạng thái tiện ích thất bại.");
        }
    }

    @PostMapping("/upload-icon")
    public ResponseEntity<?> uploadIcon(@RequestParam Long id, @RequestParam MultipartFile icon) {
        try {
            this.roomAmenitiesService.uploadIcon(id, icon);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Upload icon thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Upload icon thất bại.");
        }
    }

    @PostMapping("/delete-icon")
    public ResponseEntity<?> deleteIcon(@RequestParam Long id) {
        try {
            this.roomAmenitiesService.deleteIcon(id);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Xóa icon thành công.")
                            .build()
            );
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Xóa icon thất bại.");
        }
    }
}
