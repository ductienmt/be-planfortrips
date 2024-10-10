package com.be_planfortrips.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class Utils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
    public static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }


    // check định dạng ảnh
    public static void isValidImage(MultipartFile image) {
        String[] validExtensions = {".png", ".jpeg", ".jpg", ".JPG", ".JPEG"};
        String originalFilename = image.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new MessageDescriptorFormatException("Tên tệp tin không hợp lệ");
        }

        boolean isValid = Arrays.stream(validExtensions)
                .anyMatch(originalFilename::endsWith);

        if (!isValid) {
            throw new MessageDescriptorFormatException("Định dạng ảnh không đúng");
        }
    }


    public static void checkSize(MultipartFile file) {
        long maxSize = 500 * 1024; // Giới hạn dung lượng tối đa (500KB)

        if (file.getSize() > maxSize) {
            throw new MessageDescriptorFormatException("Dung lượng ảnh vượt quá giới hạn.");
        }
    }


    @SneakyThrows
    public static String saveImage(MultipartFile image) {
        Path staticPath = Paths.get("uploads");
        Path imagePath = Paths.get("images");


        // Tạo thư mục nếu chưa tồn tại
        Path directory = Utils.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Thêm dấu thời gian (timestamp) vào tên file
        String uniqueFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

        // Lưu file vào thư mục
        Path filePath = directory.resolve(Objects.requireNonNull(uniqueFileName));
        try (OutputStream os = Files.newOutputStream(filePath)) {
            os.write(image.getBytes());
        }

        // Trả về tên ảnh
//        String imageUrl = resolve(uniqueFileName).toString();
        //return imageUrl.replace("\\", "/"); // Đổi dấu "\\" thành "/"
        return uniqueFileName;
    }



}
