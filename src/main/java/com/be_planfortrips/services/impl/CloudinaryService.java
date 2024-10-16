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
import java.util.HashMap;
import java.util.Map;
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CloudinaryService implements ICloudinaryService {
    Cloudinary cloudinary;
    @Override
    public Map<String, Object> uploadFile(MultipartFile file, String folder) throws IOException {
        byte[] fileBytes = file.getBytes();
        Map<String, Object> params = new HashMap<>();
        params.put("resource_type", "auto");
        params.put("folder", folder);
        Map<String, Object> uploadResult = cloudinary.uploader().upload(fileBytes, params);
        return uploadResult;
    }

    @Override
    public Map<String, Object> deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
    public String getPublicIdFromUrl(String url) {
            String[] parts = url.split("/");
            String fileNameWithExtension = parts[parts.length - 1];
            String fileName = fileNameWithExtension.split("\\.")[0];
            String folderPath = parts[parts.length - 2];
            return folderPath + "/" + fileName;
        }
}
