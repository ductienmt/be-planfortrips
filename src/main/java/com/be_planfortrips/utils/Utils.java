package com.be_planfortrips.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

@Component
public class Utils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(\\+84|0)(?:[0-9] ?){8,13}[0-9]$");


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
        long maxSize = 500 * 1024 * 1024; // Giới hạn dung lượng tối đa (5MB)

        if (file.getSize() > maxSize) {
            throw new MessageDescriptorFormatException("Dung lượng ảnh vượt quá giới hạn.");
        }
    }





}
