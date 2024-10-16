package com.be_planfortrips.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ICloudinaryService {
    Map<String, Object> uploadFile(MultipartFile file, String folder) throws IOException;
    Map<String, Object> deleteFile(String publicId) throws IOException;
    String getPublicIdFromUrl(String url);
}
