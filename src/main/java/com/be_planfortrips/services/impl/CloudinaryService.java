package com.be_planfortrips.services.impl;

import com.be_planfortrips.services.interfaces.ICloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CloudinaryService implements ICloudinaryService
{
    Cloudinary cloudinary;
    @Override
    public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        Map<String, Object> uploadResult = cloudinary.uploader().upload(fileBytes,
                ObjectUtils.asMap("resource_type", "auto"));

        return uploadResult;
    }

    @Override
    public Map<String, Object> deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
