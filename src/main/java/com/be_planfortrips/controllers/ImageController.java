package com.be_planfortrips.controllers;

import com.be_planfortrips.entity.User;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.services.interfaces.IImageService;
import com.be_planfortrips.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;


    @GetMapping()
    public ResponseEntity<byte[]> getImage(@RequestParam("id") Long id) {
        try {
            String imageUrl = this.imageRepository.findById(id).get().getUrl();
            Path path = Paths.get("src/main/resources/static/images/" + imageUrl);

            // Kiểm tra xem file có tồn tại không
            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build(); // Trả về 404 nếu file không tồn tại
            }

            byte[] imageBytes = Files.readAllBytes(path);

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
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

