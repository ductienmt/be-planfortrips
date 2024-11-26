package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.CheckinDto;
import com.be_planfortrips.dto.response.CheckinResponse;
import com.be_planfortrips.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ICheckinService {
    CheckinResponse createCheckin(CheckinDto checkinDto);
    CheckinResponse updateCheckin(Long id, CheckinDto checkinDto);
    void deleteCheckin(Long id);
    CheckinResponse getCheckin(Long id);
    Map<String, Object> getAllCheckin(Integer page);
    Map<String, Object>getCheckinByCityName(String cityName, Integer page);
    Map<String, Object> getCheckinByName(String checkinName, Integer page);
    void uploadImage(Long checkinId, List<MultipartFile> files);
    List<CheckinResponse> getCheckinRandom(Integer limit,String cityName);
    List<Image> getImagesByCheckinId(Long checkinId);
    List<CheckinResponse> getCheckinByCityId(String cityId);
}
