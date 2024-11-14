package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.HotelAmenitiesDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.HotelAmenitiesResponse;
import com.be_planfortrips.services.interfaces.IHotelAmenitiesService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/hotel-amenities")
@Slf4j
public class HotelAmenitiesController {
    @Autowired
    private IHotelAmenitiesService hotelAmenitiesService;

    // Phương thức hỗ trợ để tạo ApiResponse
    private ResponseEntity<ApiResponse<Void>> buildApiResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                ApiResponse.<Void>builder()
                        .code(status.value())
                        .message(message)
                        .build()
        );
    }

    @GetMapping("/get-by-hotel-id")
    public ResponseEntity<?> getByHotelId(@RequestParam Long hotelId, @RequestParam(defaultValue = "true", required = false) String status) {
        try {
            if (hotelId == null) {
                throw new Exception("Hotel id is required");
            }
            return ResponseEntity.ok(
                    ApiResponse.<List<HotelAmenitiesResponse>>builder()
                            .code(HttpStatus.OK.value())
                            .data(this.hotelAmenitiesService.getByHotelId(hotelId, status))
                            .message("Lấy danh sách tiện ích thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Lấy danh sách tiện ích thất bại.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HotelAmenitiesDto hotelAmenities) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.<HotelAmenitiesResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(this.hotelAmenitiesService.create(hotelAmenities))
                            .message("Tạo tiện ích thành công.")
                            .build()
            );
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Tạo tiện ích thất bại.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody HotelAmenitiesDto hotelAmenities) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.<HotelAmenitiesResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(this.hotelAmenitiesService.update(id, hotelAmenities))
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
            this.hotelAmenitiesService.delete(id);
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
            this.hotelAmenitiesService.changeStatus(id);
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
            this.hotelAmenitiesService.uploadIcon(id, icon);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Upload icon thành công.")
                            .build()
            );
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Upload icon thất bại.");
        }
    }

    @PostMapping("/delete-icon")
    public ResponseEntity<?> deleteIcon(@RequestParam Long id) {
        try {
            this.hotelAmenitiesService.deleteIcon(id);
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
