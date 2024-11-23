package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.dto.response.rsTourResponse.TourScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface ITourService {
    TourResponse createTour(TourDTO TourDTO) throws Exception;
    TourResponse updateTour(Integer id,TourDTO TourDTO) throws Exception;
    Page<TourResponse> getActiveTours(PageRequest request, String title,Integer rating, List<String> tags);
    TourResponse getByTourId(Integer id) throws Exception;
    void deleteTourById(Integer id);
    TourResponse uploadImage(Integer id, List<MultipartFile> files) throws Exception;
    TourResponse deleteImage(Integer id,  List<Integer> imageIds) throws Exception;
    List<TourScheduleResponse> getScheduleAvailable(LocalDateTime day, String cityId);
}
