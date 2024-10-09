package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.HotelImageDto;
import com.be_planfortrips.dto.response.HotelImageResponse;
import com.be_planfortrips.dto.response.HotelListResponse;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.services.interfaces.IHotelService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/hotels")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class HotelController {
    IHotelService iHotelService;

    @PostMapping("")
    public ResponseEntity<?> createHotel(@Valid @RequestBody HotelDto hotelDto,
                                         BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            HotelResponse hotelResponse = iHotelService.createHotel(hotelDto);
            return ResponseEntity.ok(hotelResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("error"+e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseEntity<HotelListResponse> getHotels(@RequestParam("page")     int page,
                                                       @RequestParam("limit")    int limit){
        PageRequest request = PageRequest.of(page, limit,
                Sort.by("rating").ascending());
        int totalPage = 0;
        Page<HotelResponse> hotelResponses = iHotelService.getAllHotel(request);
        totalPage = hotelResponses.getTotalPages();
        return ResponseEntity.ok(HotelListResponse.builder()
                        .hotelResponseList(hotelResponses.toList())
                        .totalPage(totalPage)
                        .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@Valid @RequestBody HotelDto hotelDto,
                                         @PathVariable Long id,
                                         BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            HotelResponse updateHotel = iHotelService.updateHotel(id,hotelDto);
            return ResponseEntity.ok(updateHotel);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long id) {
        iHotelService.deleteHotelById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id){
        try {
            HotelResponse hotelResponse = iHotelService.getByHotelId(id);
            return ResponseEntity.ok(hotelResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable long id,@RequestParam("files") List<MultipartFile> files)throws IllegalArgumentException{
        try{
            HotelResponse existingHotel = iHotelService.getByHotelId(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            List<HotelImageResponse> list = new ArrayList<>();
            for (MultipartFile file : files) {
                if(file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                // Lưu file và cập nhật thumbnail trong DTO
                String filename = storeFile(file); // Thay thế hàm này với code của bạn để lưu file
                //lưu vào đối tượng product trong DB => sẽ làm sau
                HotelImageResponse hotelImageResponse = iHotelService.createHotelImage(
                        id,
                        HotelImageDto
                                .builder()
                                .hotelId(id)
                                .imageUrl(filename)
                                .build()
                );
                list.add(hotelImageResponse);
            }
            return ResponseEntity.ok(list);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable("imageName") String imageName){
        try {
            Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource urlResource = new UrlResource(imagePath.toUri());
            if(urlResource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(urlResource);
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
}
