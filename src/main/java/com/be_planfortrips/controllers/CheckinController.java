package com.be_planfortrips.controllers;


import com.be_planfortrips.dto.CheckinDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.CheckinResponse;
import com.be_planfortrips.services.interfaces.ICheckinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("${api.prefix}/check-in")
public class CheckinController {
    @Autowired
    private ICheckinService checkinService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCheckin(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page) {
        try {
            Map<String, Object> checkins = this.checkinService.getAllCheckin(page);
            if (checkins.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponse.<Void>builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .message("Không có điểm checkin.")
                                .build()
                );
            }

            return ResponseEntity.ok(
                    ApiResponse.<Map<String, Object>>builder()
                            .code(HttpStatus.OK.value())
                            .data(checkins)
                            .message("Lấy danh sách điểm checkin thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Lấy danh sách điểm checkin thất bại.")
                            .build()
            );
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getCheckin(@RequestParam(value = "id") Long id) {
        try {
            CheckinResponse checkin = this.checkinService.getCheckin(id);
            return ResponseEntity.ok(
                    ApiResponse.<CheckinResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(checkin)
                            .message("Lấy thông tin điểm checkin thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Lấy thông tin điểm checkin thất bại.")
                            .build()
            );
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getCheckinByCityName(@RequestParam(value = "cityName", required = false) String cityName,
                                                  @RequestParam(value = "checkinName", required = false) String checkinName,
                                                  @RequestParam(value = "page", defaultValue = "1", required = false) Integer page) {
        try {
            Map<String, Object> checkins = new HashMap<>();

            // Tìm kiếm theo cityName nếu có
            if (cityName != null) {
                checkins = this.checkinService.getCheckinByCityName(cityName, page);
                if (checkins.isEmpty()) {
                    return buildApiResponse(HttpStatus.NOT_FOUND, "Không có điểm checkin.");
                }
            }

            // Tìm kiếm theo checkinName nếu có
            if (checkinName != null) {
                checkins = this.checkinService.getCheckinByName(checkinName, page);
                if (checkins.isEmpty()) {
                    return buildApiResponse(HttpStatus.NOT_FOUND, "Không có điểm checkin.");
                }
            }

            return ResponseEntity.ok(
                    ApiResponse.<Map<String, Object>>builder()
                            .code(HttpStatus.OK.value())
                            .data(checkins)
                            .message("Lấy danh sách điểm checkin thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e); // In ra chi tiết lỗi
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy danh sách điểm checkin thất bại.");
        }
    }

    // Phương thức hỗ trợ để tạo ApiResponse
    private ResponseEntity<ApiResponse<Void>> buildApiResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                ApiResponse.<Void>builder()
                        .code(status.value())
                        .message(message)
                        .build()
        );
    }


    @PostMapping("/create")
    public ResponseEntity<?> createCheckin(@RequestBody CheckinDto checkinDto) {
        try {
            CheckinResponse checkinResponse = this.checkinService.createCheckin(checkinDto);
            return ResponseEntity.ok(
                    ApiResponse.<CheckinResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(checkinResponse)
                            .message("Tạo điểm checkin thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Tạo điểm checkin thất bại.")
                            .build()
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCheckin(@RequestParam("id") Long id, @RequestBody CheckinDto checkinDto) {
        try {
            CheckinResponse checkinResponse = this.checkinService.updateCheckin(id, checkinDto);
            return ResponseEntity.ok(
                    ApiResponse.<CheckinResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(checkinResponse)
                            .message("Cập nhật điểm checkin thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Cập nhật điểm checkin thất bại.")
                            .build()
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCheckin(@RequestParam("id") Long id) {
        try {
            this.checkinService.deleteCheckin(id);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Xóa điểm checkin thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Xóa điểm checkin thất bại.")
                            .build()
            );
        }
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("checkinId") Long checkinId, @RequestParam("files") List<MultipartFile> files) {
        try {
            this.checkinService.uploadImage(checkinId, files);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Upload ảnh thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Upload ảnh thất bại.")
                            .build()
            );
        }
    }


}