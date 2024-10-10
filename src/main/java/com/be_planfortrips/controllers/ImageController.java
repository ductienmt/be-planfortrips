package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository;



    @GetMapping()
    public ResponseEntity<?> getImage(@RequestParam("id") Long id) {
        try {
            String imageUrl;
            try {
                imageUrl = this.imageRepository.findById(id).get().getUrl();
            }
            catch (Exception e) {
                log.error(e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponse.<Void>builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .message("File không tồn tại.")
                                .build()
                );
            }
            Path path = Paths.get("uploads/images/" + imageUrl);

            // Kiểm tra xem file có tồn tại không
            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponse.<Void>builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .message("File không tồn tại.")
                                .build()
                ); // Trả về 404 nếu file không tồn tại
            }

            UrlResource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponse.<Void>builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .message("File không tồn tại.")
                                .build()
                );
            }
//            byte[] imageBytes = Files.readAllBytes(path);

            // Lấy phần mở rộng của file để xác định Content-Type
            String fileExtension = FilenameUtils.getExtension(imageUrl);
            MediaType mediaType;

            // Xác định Content-Type dựa trên phần mở rộng
            switch (fileExtension.toLowerCase()) {
                case "jpg":
                case "jpeg":
                    mediaType = MediaType.IMAGE_JPEG;
                    break;
                case "png":
                    mediaType = MediaType.IMAGE_PNG;
                    break;
                default:
                    log.error("Không hỗ trợ định dạng file: " + fileExtension);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            ApiResponse.<Void>builder()
                                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .message("Không hỗ trợ định dạng file.")
                                    .build()
                    );
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Lỗi khi đọc file.")
                            .build()
            );
        }
    }
}

