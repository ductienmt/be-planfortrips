package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.BannerDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.BannerResponseAdmin;
import com.be_planfortrips.dto.response.BannerResponseUser;
import com.be_planfortrips.services.interfaces.IBannerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/banners")
@Slf4j
public class BannerController {
    @Autowired
    private IBannerService bannerService;

    // Phương thức hỗ trợ để tạo ApiResponse
    private ResponseEntity<ApiResponse<Void>> buildApiResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                ApiResponse.<Void>builder()
                        .code(status.value())
                        .message(message)
                        .build()
        );
    }

    @GetMapping("/allUser")
    public ResponseEntity<?> getAllBannersUser() {
        try {
            List<BannerResponseUser> banners = this.bannerService.getAllBannersUser();

            return ResponseEntity.ok(
                    ApiResponse.<List<BannerResponseUser>>builder()
                            .code(HttpStatus.OK.value())
                            .data(banners)
                            .message("")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy danh sách banner thất bại.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBanners() {
        try {
            List<BannerResponseAdmin> banners = this.bannerService.getAllBanners();

            return ResponseEntity.ok(
                    ApiResponse.<List<BannerResponseAdmin>>builder()
                            .code(HttpStatus.OK.value())
                            .data(banners)
                            .message("")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy danh sách banner thất bại.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBanner(@Valid @RequestBody BannerDto bannerDto) {
        try {
            BannerResponseAdmin bannerResponse = this.bannerService.createBanner(bannerDto);
            return ResponseEntity.ok(
                    ApiResponse.<BannerResponseAdmin>builder()
                            .code(HttpStatus.OK.value())
                            .data(bannerResponse)
                            .message("Tạo banner thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Tạo banner thất bại.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBanner(@RequestParam("id") Long id,@Valid @RequestBody BannerDto bannerDto) {
        try {
            BannerResponseAdmin bannerResponse = this.bannerService.updateBanner(id, bannerDto);
            return ResponseEntity.ok(
                    ApiResponse.<BannerResponseAdmin>builder()
                            .code(HttpStatus.OK.value())
                            .data(bannerResponse)
                            .message("Cập nhật banner thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Cập nhật banner thất bại.");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteBanner(@RequestParam("id") Long id) {
        try {
            this.bannerService.deleteBanner(id);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Xóa banner thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Xóa banner thất bại.");
        }
    }

    @PatchMapping("/setStage")
    public ResponseEntity<?> setStageBanner(@RequestParam("id") Long id, @RequestParam("stage") Integer stage) {
        try {
            this.bannerService.changeStatusBanner(id, stage);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Cập nhật trạng thái banner thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Cập nhật trạng thái banner thất bại.");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("id") Long id, @RequestParam("image") MultipartFile image) {
        try {
            this.bannerService.uploadBannerImage(id, image);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Upload ảnh banner thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Upload ảnh banner thất bại.");
        }
    }
}
