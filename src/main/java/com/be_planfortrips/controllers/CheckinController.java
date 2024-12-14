package com.be_planfortrips.controllers;


import com.be_planfortrips.dto.CheckinDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.CheckinResponse;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.services.interfaces.ICheckinService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

            return ResponseEntity.ok(
                    ApiResponse.<Map<String, Object>>builder()
                            .code(HttpStatus.OK.value())
                            .data(checkins)
                            .message("Lấy danh sách điểm checkin thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy danh sách điểm checkin thất bại.");
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getCheckin(@RequestParam(value = "id") Long id) {
        try {
            System.out.println("checkinId: " + id);
            CheckinResponse checkin = this.checkinService.getCheckin(id);
            return ResponseEntity.ok(
                    ApiResponse.<CheckinResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(checkin)
                            .message("Lấy thông tin điểm checkin thành công.")
                            .build()
            );
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
    public ResponseEntity<?> createCheckin(@Valid @RequestBody CheckinDto checkinDto) {
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
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Tạo điểm checkin thất bại.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCheckin(@RequestParam("id") Long id,@Valid @RequestBody CheckinDto checkinDto) {
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
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Cập nhật điểm checkin thất bại.");
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
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Xóa điểm checkin thất bại.");
        }
    }

    @PostMapping(value = "/upload-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("checkinId") Long checkinId, @RequestPart(value = "files",required = false) List<MultipartFile> files) {
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
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Upload ảnh thất bại.");
        }
    }

    @GetMapping("getImages")
    public ResponseEntity<?> getImagesByCheckinId(@RequestParam("checkinId") Long checkinId) {
        try {
            List<Image> images = this.checkinService.getImagesByCheckinId(checkinId);
            return ResponseEntity.ok(
                    ApiResponse.<List<Image>>builder()
                            .code(HttpStatus.OK.value())
                            .data(images)
                            .message("Lấy danh sách ảnh thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy danh sách ảnh thất bại.");
        }
    }

    @GetMapping("getByCityId")
    public ResponseEntity<?> getCheckinByCityId(@RequestParam("cityId") String city) {
        try {
            List<CheckinResponse> checkins = this.checkinService.getCheckinByCityId(city);
            return ResponseEntity.ok(
                    ApiResponse.<List<CheckinResponse>>builder()
                            .code(HttpStatus.OK.value())
                            .data(checkins)
                            .message("Lấy danh sách điểm checkin thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy danh sách điểm checkin thất bại.");
        }
    }



}
